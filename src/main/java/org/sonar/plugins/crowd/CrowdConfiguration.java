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

import org.apache.commons.configuration.Configuration;
import org.sonar.api.ServerExtension;

import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class CrowdConfiguration implements ServerExtension {
    private final Configuration configuration;
    private Properties clientProperties;

    /**
     * Creates new instance of CrowdConfiguration.
     *
     * @param configuration configuration
     */
    public CrowdConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Returns Crowd client properties.
     *
     * @return Crowd client properties
     */
    public Properties getClientProperties() {
        if (clientProperties == null) {
            clientProperties = newInstance();
        }
        return clientProperties;
    }

    private Properties newInstance() {
        final String crowdUrl = configuration.getString("crowd.url", null);
        final String applicationName = configuration.getString("crowd.application", "sonar");
        final String applicationPassword = configuration.getString("crowd.password", null);

        if (crowdUrl == null) {
            throw new IllegalArgumentException("Crowd URL is not set");
        }
        if (applicationName == null) {
            throw new IllegalArgumentException("Crowd Application Name is not set");
        }
        if (applicationPassword == null) {
            throw new IllegalArgumentException("Crowd Application Password is not set");
        }

        if (CrowdHelper.LOG.isInfoEnabled()) {
            CrowdHelper.LOG.info("URL: " + crowdUrl);
            CrowdHelper.LOG.info("Application Name: " + applicationName);
        }

        Properties properties = new Properties();
        properties.setProperty("crowd.server.url", crowdUrl);
        properties.setProperty("application.name", applicationName);
        properties.setProperty("application.password", applicationPassword);
        properties.setProperty("session.validationinterval", "5");
        return properties;
    }
}
