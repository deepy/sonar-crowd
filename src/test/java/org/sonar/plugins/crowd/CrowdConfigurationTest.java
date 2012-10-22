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

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class CrowdConfigurationTest {

  private Settings settings;
  private CrowdConfiguration crowdConfiguration;

  @Before
  public void setUp() {
    settings = new Settings();
    crowdConfiguration = new CrowdConfiguration(settings);
  }

  @Test(expected = IllegalArgumentException.class)
  public void crowdUrlMissing() {
    crowdConfiguration.getClientProperties();
  }

  @Test(expected = IllegalArgumentException.class)
  public void applicationPasswordMissing() {
    settings.setProperty("crowd.url", "http://localhost:8095/crowd");
    crowdConfiguration.getClientProperties();
  }

  @Test
  public void shouldCreateClientProperties() {
    settings.setProperty("crowd.url", "http://localhost:8095/crowd");
    settings.setProperty("crowd.password", "secure");

    Properties properties = crowdConfiguration.getClientProperties();
    assertThat("client properties should be cached", crowdConfiguration.getClientProperties(), sameInstance(properties));
    assertThat(properties.getProperty("crowd.server.url"), is("http://localhost:8095/crowd"));
    assertThat("default application.name", properties.getProperty("application.name"), is("sonar"));
    assertThat(properties.getProperty("application.password"), is("secure"));
    assertThat(properties.getProperty("session.validationinterval"), is("5"));
  }

}
