package org.sonar.plugins.crowd;

import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.service.client.ClientProperties;
import com.atlassian.crowd.service.client.ClientPropertiesImpl;
import com.atlassian.crowd.service.client.CrowdClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.ServerSide;
import org.sonar.api.server.authentication.BaseIdentityProvider;
import org.sonar.api.server.authentication.Display;
import org.sonar.api.server.authentication.UserIdentity;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

@ServerSide
public class CrowdSsoIdentityProvider implements BaseIdentityProvider {
    private static final Logger LOG = LoggerFactory.getLogger(CrowdSsoIdentityProvider.class);
    private final CrowdClient client;
    private final CrowdConfiguration configuration;


    public CrowdSsoIdentityProvider(CrowdConfiguration configuration) {
        this.configuration = configuration;
        this.client = createCrowdClient(configuration);
    }

    private CrowdClient createCrowdClient(CrowdConfiguration configuration) {
        Properties crowdProperties = new Properties();
        // The name that the application will use when authenticating with the Crowd server.
        crowdProperties.setProperty("application.name", configuration.getCrowdApplicationName());
        // The password that the application will use when authenticating with the Crowd server.
        crowdProperties.setProperty("application.password", configuration.getCrowdApplicationPassword());
        // Crowd will redirect the user to this URL if their authentication token expires or is invalid due to security restrictions.
        // crowdProperties.setProperty("application.login.url", "");
        // The URL to use when connecting with the integration libraries to communicate with the Crowd server.
        // crowdProperties.setProperty("crowd.server.url", "");
        // The URL used by Crowd to create the full URL to be sent to users that reset their passwords.
        crowdProperties.setProperty("crowd.base.url", configuration.getCrowdUrl());
        // The session key to use when storing a Boolean value indicating whether the user is authenticated or not.
        crowdProperties.setProperty("session.isauthenticated", "session.isauthenticated");
        // The session key to use when storing a String value of the user's authentication token.
        crowdProperties.setProperty("session.tokenkey", "session.tokenkey");
        // The number of minutes to cache authentication validation in the session. If this value is set to 0, each HTTP request will be
        // authenticated with the Crowd server.
        crowdProperties.setProperty("session.validationinterval", "1");
        // The session key to use when storing a Date value of the user's last authentication.
        crowdProperties.setProperty("session.lastvalidation", "session.lastvalidation");
        // Perhaps more things to let users to configure in the future
        // (see https://confluence.atlassian.com/display/CROWD/The+crowd.properties+file)
        ClientProperties clientProperties = ClientPropertiesImpl.newInstanceFromProperties(crowdProperties);
        return new RestCrowdClientFactory().newInstance(clientProperties);
    }

    @Override
    public void init(Context context) {
    try {
        Cookie[] cookies = context.getRequest().getCookies();
        Optional<String> ssoToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("crowd.token_key"))
                .map(Cookie::getValue)
                .findFirst();
        if (ssoToken.isPresent()) {
            User user = client.findUserFromSSOToken(ssoToken.get());
            context.authenticate(UserIdentity.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmailAddress())
                    .setLogin(user.getExternalId())
                    .setProviderLogin(getKey())
                    .build());
        }
    } catch (
    ApplicationPermissionException e) {
        LOG.error("The application is not permitted to perform the requested operation"
                + " on the crowd server", e);
        return;
    } catch (
    InvalidAuthenticationException e) {
        LOG.debug("Invalid credentials for user", e);
        return;
    } catch (OperationFailedException e) {
        LOG.error("Unable to authenticate user", e);
        return;
    } catch (InvalidTokenException e) {
        LOG.error("Invalid token", e);
        return;
    }
    }

    @Override
    public String getKey() {
        return "CrowdSSO";
    }

    @Override
    public String getName() {
        return "CrowdSSO";
    }

    @Override
    public Display getDisplay() {
        return Display.builder()
                .setIconPath("/static/crowd/crowd.svg")
                .setBackgroundColor("#2F2F2F")
                .build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean allowsUsersToSignUp() {
        return false;
    }
}
