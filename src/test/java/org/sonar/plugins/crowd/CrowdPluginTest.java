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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Evgeny Mandrikov
 */
public class CrowdPluginTest {
  private CrowdPlugin plugin; 

  @Before
  public void setUp() throws Exception {
    plugin = new CrowdPlugin();
  }

  @Test
  public void test() {
    assertNotNull(plugin.getKey());
    assertNotNull(plugin.getName());
    assertNotNull(plugin.getDescription());
    assertEquals(plugin.getExtensions().size(), 2);
  }
}
