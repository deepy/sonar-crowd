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
