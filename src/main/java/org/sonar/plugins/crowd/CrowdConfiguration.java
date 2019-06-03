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

import org.sonar.api.config.Configuration;
import org.sonar.api.server.ServerSide;

/**
 * @author Evgeny Mandrikov
 */
@ServerSide
public class CrowdConfiguration {

  static final String KEY_CROWD_URL = "crowd.url";
  static final String KEY_CROWD_APP_NAME = "crowd.application";
  static final String KEY_CROWD_APP_PASSWORD = "crowd.password";
  static final String FALLBACK_NAME = "sonar";
  private final Configuration settings;

  /**
   * Creates new instance of CrowdConfiguration.
   *
   * @param settings configuration
   */
  public CrowdConfiguration(Configuration settings) {
    this.settings = settings;
  }

  /**
   * The name that the application will use when authenticating with the Crowd server.<br>
   * Uses the settings key {@value #KEY_CROWD_APP_NAME}
   */
  public String getCrowdApplicationName() {
    return settings.get(KEY_CROWD_APP_NAME).orElse(FALLBACK_NAME);
  }

  /**
   * The password that the application will use when authenticating with the Crowd server.<br>
   * Uses the settings key {@value #KEY_CROWD_APP_PASSWORD}
   */
  public String getCrowdApplicationPassword() {
    return settings
            .get(KEY_CROWD_APP_PASSWORD)
            .orElseThrow(() -> new IllegalArgumentException(KEY_CROWD_APP_PASSWORD + " is not set"));
  }

  /**
   * The base URL of the crowd server, e.g. <a href="http://127.0.0.1:8095/crowd">http://127.0.0.1:8095/crowd</a>}.<br>
   * Uses the settings key {@value #KEY_CROWD_URL}
   */
  public String getCrowdUrl() {
    return settings
            .get(KEY_CROWD_URL)
            .orElseThrow(() -> new IllegalArgumentException(KEY_CROWD_URL + " is not set"));
  }
}
