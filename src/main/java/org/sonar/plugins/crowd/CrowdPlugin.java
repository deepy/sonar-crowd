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

import org.sonar.api.Extension;
import org.sonar.api.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
@SuppressWarnings({"UnusedDeclaration"})
public class CrowdPlugin implements Plugin {
    public String getKey() {
        return "crowd";
    }

    public String getName() {
        return "Crowd";
    }

    public String getDescription() {
        return "Plugs authentication mechanism to a Crowd to delegate passwords management.";
    }

    public List<Class<? extends Extension>> getExtensions() {
        ArrayList<Class<? extends Extension>> extensions = new ArrayList<Class<? extends Extension>>();
        extensions.add(CrowdAuthenticator.class);
        extensions.add(CrowdConfiguration.class);
        return extensions;
    }
}
