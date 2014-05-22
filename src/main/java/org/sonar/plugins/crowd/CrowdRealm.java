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

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.service.client.CrowdClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.LoginPasswordAuthenticator;
import org.sonar.api.security.SecurityRealm;
import org.sonar.api.utils.SonarException;

/**
 * Sonar security realm for crowd.
 */
public class CrowdRealm extends SecurityRealm {

  private static final Logger LOG = LoggerFactory.getLogger(CrowdRealm.class);

  private final CrowdClient crowdClient;
  private final CrowdAuthenticator authenticator;
  private final CrowdUsersProvider usersProvider;
  private final CrowdGroupsProvider groupsProvider;

  public CrowdRealm(CrowdConfiguration crowdConfiguration) {
    this.crowdClient = createCrowdClient(crowdConfiguration);
    this.authenticator = new CrowdAuthenticator(crowdClient);
    this.usersProvider = new CrowdUsersProvider(crowdClient);
    this.groupsProvider = new CrowdGroupsProvider(crowdClient);
  }

  private CrowdClient createCrowdClient(CrowdConfiguration configuration) {
    String crowdUrl = configuration.getCrowdUrl();
    String applicationName = configuration.getCrowdApplicationName();
    String applicationPassword = configuration.getCrowdApplicationPassword();

    LOG.info("Crowd URL: " + crowdUrl);
    LOG.info("Crowd application name: " + applicationName);

    return new RestCrowdClientFactory().newInstance(crowdUrl, applicationName, applicationPassword);
  }

  @Override
  public String getName() {
    return "Crowd";
  }

  @Override
  public void init() {
    try {
      crowdClient.testConnection();
    } catch (OperationFailedException e) {
      throw new SonarException("Unable to test connection to crowd", e);
    } catch (InvalidAuthenticationException e) {
      throw new SonarException("Application name and password are incorrect", e);
    } catch (ApplicationPermissionException e) {
      throw new SonarException("The application is not permitted to perform the requested "
          + "operation on the crowd server", e);
    }
  }

  @Override
  public LoginPasswordAuthenticator getLoginPasswordAuthenticator() {
    return authenticator;
  }

  @Override
  public ExternalGroupsProvider getGroupsProvider() {
    return groupsProvider;
  }

  @Override
  public ExternalUsersProvider getUsersProvider() {
    return usersProvider;
  }

}
