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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;

import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class CrowdConfiguration implements ServerExtension {

  private static final Logger LOG = LoggerFactory.getLogger(CrowdConfiguration.class);

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

    if (LOG.isInfoEnabled()) {
      LOG.info("Crowd URL: " + crowdUrl);
      LOG.info("Crowd application name: " + applicationName);
    }

    Properties properties = new Properties();
    properties.setProperty("crowd.server.url", crowdUrl);
    properties.setProperty("application.name", applicationName);
    properties.setProperty("application.password", applicationPassword);

    // might be a good idea to make this configurable
    properties.setProperty("session.validationinterval", "5");
    return properties;
  }
}
