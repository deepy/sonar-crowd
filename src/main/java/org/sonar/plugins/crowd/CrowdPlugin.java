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
    @Override
    public String getKey() {
        return "crowd";
    }

    @Override
    public String getName() {
        return "Crowd";
    }

    @Override
    public String getDescription() {
        return "Plugs authentication mechanism to a Crowd to delegate passwords management.";
    }

    @Override
    public List<Class<? extends Extension>> getExtensions() {
        ArrayList<Class<? extends Extension>> extensions = new ArrayList<Class<? extends Extension>>();
        extensions.add(CrowdAuthenticator.class);
        return extensions;
    }
}
