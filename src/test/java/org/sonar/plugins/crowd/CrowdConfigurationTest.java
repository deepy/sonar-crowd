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

import org.junit.Test;
import org.sonar.api.config.Settings;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CrowdConfigurationTest {

  @Test(expected = IllegalArgumentException.class)
  public void crowdUrlMissing() {
    Settings settings = new Settings();
    new CrowdConfiguration(settings).getCrowdUrl();
  }

  @Test(expected = IllegalArgumentException.class)
  public void applicationPasswordMissing() {
    Settings settings = new Settings();
    settings.setProperty(CrowdConfiguration.KEY_CROWD_URL, "http://localhost:8095");
    new CrowdConfiguration(settings).getCrowdApplicationPassword();
  }

  @Test
  public void usesFallbackForUnsetApplicationName() {
    Settings settings = new Settings();
    settings.setProperty(CrowdConfiguration.KEY_CROWD_URL, "http://localhost:8095");
    settings.setProperty(CrowdConfiguration.KEY_CROWD_APP_PASSWORD, "secret");
    CrowdConfiguration crowdConfiguration = new CrowdConfiguration(settings);
    assertThat(crowdConfiguration.getCrowdApplicationName(), is(CrowdConfiguration.FALLBACK_NAME));
  }

  @Test
  public void createsClientProperties() {
    Settings settings = new Settings();
    settings.setProperty(CrowdConfiguration.KEY_CROWD_URL, "http://localhost:8095");
    settings.setProperty(CrowdConfiguration.KEY_CROWD_APP_NAME, "SonarQube");
    settings.setProperty(CrowdConfiguration.KEY_CROWD_APP_PASSWORD, "secret");
    CrowdConfiguration crowdConfiguration = new CrowdConfiguration(settings);

    assertThat(crowdConfiguration.getCrowdUrl(), is("http://localhost:8095"));
    assertThat(crowdConfiguration.getCrowdApplicationName(), is("SonarQube"));
    assertThat(crowdConfiguration.getCrowdApplicationPassword(), is("secret"));
  }

}
