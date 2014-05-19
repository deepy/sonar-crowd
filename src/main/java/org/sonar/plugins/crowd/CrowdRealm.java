package org.sonar.plugins.crowd;

import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.service.client.CrowdClient;

import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.LoginPasswordAuthenticator;
import org.sonar.api.security.SecurityRealm;
import org.sonar.api.utils.SonarException;

import java.util.Properties;

/**
 * Sonar security realm for crowd.
 *  
 * @author Ferdinand Hübner
 */
public class CrowdRealm extends SecurityRealm {

  private final CrowdClient crowdClient;
  private final CrowdAuthenticator authenticator;
  private final CrowdUsersProvider usersProvider;
  private final CrowdGroupsProvider groupsProvider;

  public CrowdRealm(CrowdConfiguration crowdConfiguration) {
    this.crowdClient = createCrowdClient(crowdConfiguration);
    this.authenticator = new CrowdAuthenticator(crowdClient);
    this.usersProvider = new CrowdUsersProvider(crowdClient);
    this.groupsProvider = new CrowdGroupsProvider(crowdClient);
  }

  private CrowdClient createCrowdClient(CrowdConfiguration configuration) {
    Properties props = configuration.getClientProperties();

    String crowdUrl = props.getProperty(CrowdConfiguration.KEY_CROWD_URL);
    String applicationName = props.getProperty(CrowdConfiguration.KEY_CROWD_APP_NAME);
    String applicationPassword = props.getProperty(CrowdConfiguration.KEY_CROWD_APP_PASSWORD);

    return new RestCrowdClientFactory().newInstance(crowdUrl, applicationName, applicationPassword);
  }

  @Override
  public String getName() {
    return "Crowd";
  }

  @Override
  public void init() {
    try {
      crowdClient.testConnection();
    } catch (OperationFailedException e) {
      throw new SonarException("Unable to test connection to crowd", e);
    } catch (InvalidAuthenticationException e) {
      throw new SonarException("Application name and password are incorrect", e);
    } catch (ApplicationPermissionException e) {
      throw new SonarException("The application is not permitted to perform the requested "
          + "operation on the crowd server", e);
    }
  }

  @Override
  public LoginPasswordAuthenticator getLoginPasswordAuthenticator() {
    return authenticator;
  }

  @Override
  public ExternalGroupsProvider getGroupsProvider() {
    return groupsProvider;
  }

  @Override
  public ExternalUsersProvider getUsersProvider() {
    return usersProvider;
  }

}
