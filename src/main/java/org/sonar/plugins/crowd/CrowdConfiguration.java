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

  static final String KEY_CROWD_URL = "crowd.url";
  static final String KEY_CROWD_APP_NAME = "crowd.application";
  static final String KEY_CROWD_APP_PASSWORD = "crowd.password";
  static final String FALLBACK_NAME = "sonar";
  static final String KEY_CROWD_GRP_SYNC = "crowd.groups.sync";
  private final Settings settings;

  /**
   * Creates new instance of CrowdConfiguration.
   *
   * @param configuration configuration
   */
  public CrowdConfiguration(Settings settings) {
    this.settings = settings;
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

  /**
   * The name that the application will use when authenticating with the Crowd server.<br />
   * Uses the settings key {@value #KEY_CROWD_APP_NAME}
   */
  public String getCrowdApplicationName() {
    return get(KEY_CROWD_APP_NAME, settings, FALLBACK_NAME);
  }

  /**
   * The password that the application will use when authenticating with the Crowd server.<br />
   * Uses the settings key {@value #KEY_CROWD_APP_PASSWORD}
   */
  public String getCrowdApplicationPassword() {
    return getAndValidate(KEY_CROWD_APP_PASSWORD, settings);
  }

  /**
   * The base URL of the crowd server, e.g. {@linkplain http://127.0.0.1:8095/crowd}.<br />
   * Uses the settings key {@value #KEY_CROWD_URL}
   */
  public String getCrowdUrl() {
    return getAndValidate(KEY_CROWD_URL, settings);
  }
  
  /**
   * Define if the Groups have to be synced from the Crowd server.
   * Defaults to true if not provided.
   */
  public String getCrowdGrpSync() {
      return get(KEY_CROWD_GRP_SYNC, settings, "true");
  }
}
