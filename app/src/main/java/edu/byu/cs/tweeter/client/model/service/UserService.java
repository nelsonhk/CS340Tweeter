package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends ServiceTemplate {

    public interface LogoutObserver extends ServiceTemplate.ServiceObserver {
        void logoutSuccess();
    }

    public void logout(LogoutObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(logoutObserver));
        execute(logoutTask);
    }

    private class LogoutHandler extends BackgroundTaskHandler<LogoutObserver> {

        public LogoutHandler(LogoutObserver logoutObserver) {
            super(logoutObserver);
        }

        @Override
        protected void handleSuccessMessage(LogoutObserver observer, Bundle data) {
            observer.logoutSuccess();
        }

    }

    public interface LoginObserver extends ServiceTemplate.ServiceObserver {
        void loginSucceeded(User user, AuthToken authToken);
    }

    public interface RegisterObserver extends ServiceTemplate.ServiceObserver {
        void registerSucceeded(User user, AuthToken authToken);
    }

    public interface GetUserObserver extends ServiceTemplate.ServiceObserver {
        void getUserSucceeded(User user);
    }

    public void getUser(String userAlias, GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(getUserObserver));
        execute(getUserTask);
    }

    public void login(String username, String password, LoginObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(loginObserver));
        execute(loginTask);
    }

    public void register(String firstName, String lastName, String username,
                         String password, String imageBytesBase64, RegisterObserver registerObserver) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                username, password, imageBytesBase64, new RegisterHandler(registerObserver));
        execute(registerTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends BackgroundTaskHandler<LoginObserver> {

        public LoginHandler(LoginObserver loginObserver) {
            super(loginObserver);
        }

        @Override
        protected void handleSuccessMessage(LoginObserver observer, Bundle data) {
            User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            observer.loginSucceeded(loggedInUser, authToken);
        }
    }

    // RegisterHandler
    private class RegisterHandler extends BackgroundTaskHandler<RegisterObserver> {

        public RegisterHandler(RegisterObserver registerObserver) {
            super(registerObserver);
        }

        @Override
        protected void handleSuccessMessage(RegisterObserver observer, Bundle data) {
            User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);

            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            observer.registerSucceeded(registeredUser, authToken);
        }
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends BackgroundTaskHandler<GetUserObserver> {

        public GetUserHandler(GetUserObserver getUserObserver) {
            super(getUserObserver);
        }

        @Override
        protected void handleSuccessMessage(GetUserObserver observer, Bundle data) {
            User user = (User) data.getSerializable(GetUserTask.USER_KEY);
            observer.getUserSucceeded(user);
        }
    }

}
