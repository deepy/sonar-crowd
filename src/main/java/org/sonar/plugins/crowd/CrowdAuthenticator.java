/*
 * Copyright (C) 2009 Evgeny Mandrikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
      throw new RuntimeException(e);
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    } catch (ApplicationAccessDeniedException e) {
      CrowdHelper.LOG.error("Could not authenticate " + login + ". The user does not have access to authenticate with the Crowd application.", e);
    }
    return false;
  }
}
