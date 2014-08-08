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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.sonar.api.Extension;
import org.sonar.api.SonarPlugin;

import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class CrowdPlugin extends SonarPlugin {

  @Override
  public List<Class<? extends Extension>> getExtensions() {
    Builder<Class<? extends Extension>> builder = ImmutableList.builder();

    builder.add(CrowdRealm.class);
    builder.add(CrowdConfiguration.class);

    return builder.build();
  }
}
