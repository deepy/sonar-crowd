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

import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;

import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class CrowdConfiguration implements ServerExtension {

  static final String KEY_CROWD_URL = "crowd.url";
  static final String KEY_CROWD_APP_NAME = "crowd.application";
  static final String KEY_CROWD_APP_PASSWORD = "crowd.password";

  private final Settings settings;
  private Properties clientProperties;

  /**
   * Creates new instance of CrowdConfiguration.
   *
   * @param configuration configuration
   */
  public CrowdConfiguration(Settings settings) {
    this.settings = settings;
  }

  /**
   * Returns Crowd client properties.
   *
   * @return Crowd client properties
   */
  public Properties getClientProperties() {
    if (clientProperties == null) {
      clientProperties = newInstance();
    }
    return clientProperties;
  }

  private Properties newInstance() {
    final String crowdUrl = settings.getString(KEY_CROWD_URL);
    String applicationName = settings.getString(KEY_CROWD_APP_NAME);
    final String applicationPassword = settings.getString(KEY_CROWD_APP_PASSWORD);

    if (crowdUrl == null) {
      throw new IllegalArgumentException("Crowd URL is not set");
    }
    if (applicationName == null) {
      applicationName = "sonar";
    }
    if (applicationPassword == null) {
      throw new IllegalArgumentException("Crowd Application Password is not set");
    }

    if (CrowdHelper.LOG.isInfoEnabled()) {
      CrowdHelper.LOG.info("URL: " + crowdUrl);
      CrowdHelper.LOG.info("Application Name: " + applicationName);
    }

    Properties properties = new Properties();
    properties.setProperty("crowd.server.url", crowdUrl);
    properties.setProperty("application.name", applicationName);
    properties.setProperty("application.password", applicationPassword);
    properties.setProperty("session.validationinterval", "5");
    return properties;
  }
}
