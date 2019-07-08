/*
 * Sonar Crowd Plugin
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.crowd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.security.Authenticator;

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.ExpiredCredentialException;
import com.atlassian.crowd.exception.InactiveAccountException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.service.client.CrowdClient;

/**
 * @author Evgeny Mandrikov
 */
public class CrowdAuthenticator extends Authenticator {

  private static final Logger LOG = LoggerFactory.getLogger(CrowdAuthenticator.class);

  private final CrowdClient client;

  public CrowdAuthenticator(CrowdClient client) {
    this.client = client;
  }

  @Override
  public boolean doAuthenticate(final Context context) {
    final String username = context.getUsername();
    final String password = context.getPassword();
    return authenticate(username, password);
  }


  public boolean authenticate(String login, String password) {
    // Had to add that as from "not really a good idea" in
    // https://stackoverflow.com/questions/51518781/jaxb-not-available-on-tomcat-9-and-java-9-10
    ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      // This will enforce the crowClient to use the plugin classloader
      Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
      try {
        client.authenticateUser(login, password);
        return true;
      } catch (UserNotFoundException e) {
        LOG.debug("User {} not found", login);
        return false;
      } catch (InactiveAccountException e) {
        LOG.debug("User {} is not active", login);
        return false;
      } catch (ExpiredCredentialException e) {
        LOG.debug("Credentials of user {} have expired", login);
        return false;
      } catch (ApplicationPermissionException e) {
        LOG.error("The application is not permitted to perform the requested operation"
          + " on the crowd server", e);
        return false;
      } catch (InvalidAuthenticationException e) {
        LOG.debug("Invalid credentials for user {}", login);
        return false;
      } catch (OperationFailedException e) {
        LOG.error("Unable to authenticate user " + login, e);
        return false;
      }
    } finally {
      // Bring back the original class loader for the thread
      Thread.currentThread().setContextClassLoader(threadClassLoader);
    }
  }
}
