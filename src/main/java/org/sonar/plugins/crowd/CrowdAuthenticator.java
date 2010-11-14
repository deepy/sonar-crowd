/*
 * Sonar Crowd Plugin
 * Copyright (C) 2009 SonarSource
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

import com.atlassian.crowd.integration.authentication.PasswordCredential;
import com.atlassian.crowd.integration.authentication.UserAuthenticationContext;
import com.atlassian.crowd.integration.exception.ApplicationAccessDeniedException;
import com.atlassian.crowd.integration.exception.InactiveAccountException;
import com.atlassian.crowd.integration.exception.InvalidAuthenticationException;
import com.atlassian.crowd.integration.exception.InvalidAuthorizationTokenException;
import com.atlassian.crowd.integration.service.AuthenticationManager;
import com.atlassian.crowd.integration.service.cache.CachingManagerFactory;
import com.atlassian.crowd.integration.service.soap.client.ClientProperties;
import org.sonar.api.security.LoginPasswordAuthenticator;
import org.sonar.api.utils.SonarException;

import java.rmi.RemoteException;

/**
 * @author Evgeny Mandrikov
 */
public class CrowdAuthenticator implements LoginPasswordAuthenticator {
  private final CrowdConfiguration configuration;

  /**
   * Creates new instance of CrowdAuthenticator with specified configuration.
   *
   * @param configuration Crowd configuration
   */
  public CrowdAuthenticator(CrowdConfiguration configuration) {
    this.configuration = configuration;
  }

  public void init() {
  }

  public boolean authenticate(String login, String password) {
    try {
      AuthenticationManager authenticationManager = CachingManagerFactory.getAuthenticationManagerInstance();

      ClientProperties clientProperties = authenticationManager.getSecurityServerClient().getClientProperties();
      clientProperties.updateProperties(configuration.getClientProperties());

      UserAuthenticationContext authenticationContext = new UserAuthenticationContext();
      authenticationContext.setName(login);
      authenticationContext.setCredential(new PasswordCredential(password));

      authenticationManager.authenticate(authenticationContext);
      return true;
    } catch (InvalidAuthenticationException e) {
      CrowdHelper.LOG.error("Could not authenticate " + login + ". The username or password were incorrect.", e);
    } catch (InactiveAccountException e) {
      CrowdHelper.LOG.error("Could not authenticate " + login + ". The account is inactive and the user is not allowed to login.", e);
    } catch (InvalidAuthorizationTokenException e) {
      throw new SonarException(e);
    } catch (RemoteException e) {
      throw new SonarException(e);
    } catch (ApplicationAccessDeniedException e) {
      CrowdHelper.LOG.error("Could not authenticate " + login + "." +
          " The user does not have access to authenticate with the Crowd application.", e);
    }
    return false;
  }
}
