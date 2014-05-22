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

/**
 * @author Evgeny Mandrikov
 */
public class CrowdConfiguration implements ServerExtension {

  private static final String KEY_CROWD_URL = "crowd.url";
  private static final String KEY_CROWD_APP_NAME = "crowd.application";
  private static final String KEY_CROWD_APP_PASSWORD = "crowd.password";
  private static final String FALLBACK_NAME = "sonar";

  private final String crowdUrl;
  private final String crowdApplicationName;
  private final String crowdApplicationPassword;

  /**
   * Creates new instance of CrowdConfiguration.
   *
   * @param configuration configuration
   */
  public CrowdConfiguration(Settings settings) {
    crowdUrl = getAndValidate(KEY_CROWD_URL, settings);
    crowdApplicationName = get(KEY_CROWD_APP_NAME, settings, FALLBACK_NAME);
    crowdApplicationPassword = getAndValidate(KEY_CROWD_APP_PASSWORD, settings);
  }

  private String get(String key, Settings settings, String fallback) {
    String value = settings.getString(key);
    if (value == null) {
      return fallback;
    }
    return value;
  }

  private String getAndValidate(String key, Settings settings) {
    String value = settings.getString(key);
    if (value == null) {
      throw new IllegalArgumentException(key + " is not set");
    }
    return value;
  }

  public String getCrowdApplicationName() {
    return crowdApplicationName;
  }

  public String getCrowdApplicationPassword() {
    return crowdApplicationPassword;
  }

  public String getCrowdUrl() {
    return crowdUrl;
  }
}
