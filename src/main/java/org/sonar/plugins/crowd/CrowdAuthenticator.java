package org.sonar.plugins.crowd;

import com.atlassian.crowd.integration.authentication.PasswordCredential;
import com.atlassian.crowd.integration.authentication.UserAuthenticationContext;
import com.atlassian.crowd.integration.exception.ApplicationAccessDeniedException;
import com.atlassian.crowd.integration.exception.InactiveAccountException;
import com.atlassian.crowd.integration.exception.InvalidAuthenticationException;
import com.atlassian.crowd.integration.exception.InvalidAuthorizationTokenException;
import com.atlassian.crowd.integration.service.AuthenticationManager;
import com.atlassian.crowd.integration.service.cache.CachingManagerFactory;
import org.sonar.api.security.LoginPasswordAuthenticator;

import java.rmi.RemoteException;

/**
 * @author Evgeny Mandrikov
 */
public class CrowdAuthenticator implements LoginPasswordAuthenticator {
    @Override
    public void init() {
    }

    @Override
    public boolean authenticate(String login, String password) {
        // FIXME sout
        try {
            AuthenticationManager authenticationManager = CachingManagerFactory.getAuthenticationManagerInstance();

            UserAuthenticationContext authenticationContext = new UserAuthenticationContext();
            authenticationContext.setName(login);
            authenticationContext.setCredential(new PasswordCredential(password));

            authenticationManager.authenticate(authenticationContext);
            return true;
        } catch (InvalidAuthenticationException e) {
            System.out.println("Could not authenticate " + login + ". The username or password were incorrect.");
        } catch (InactiveAccountException e) {
            System.out.println("Could not authenticate " + login + ". The account is inactive and the user is not allowed to login.");
        } catch (InvalidAuthorizationTokenException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (ApplicationAccessDeniedException e) {
            System.out.println("Could not authenticate " + login + ". The user does not have access to authenticate with the Crowd application.");
        }
        return false;
    }
}
