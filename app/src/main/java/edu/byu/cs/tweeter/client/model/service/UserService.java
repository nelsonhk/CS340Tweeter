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


//    public interface RegisterObserver extends ServiceTemplate.ServiceObserver {
//        void registerSucceeded(User user, AuthToken authToken);
//    }


    public void login(String username, String password, AuthServiceObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(loginObserver));
        execute(loginTask);
    }

    public void register(String firstName, String lastName, String username,
                         String password, String imageBytesBase64, AuthServiceObserver registerObserver) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                username, password, imageBytesBase64, new RegisterHandler(registerObserver));
        execute(registerTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends BackgroundTaskHandler<AuthServiceObserver> {

        public LoginHandler(AuthServiceObserver loginObserver) {
            super(loginObserver);
        }

        @Override
        protected void handleSuccessMessage(AuthServiceObserver observer, Bundle data) {
            User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

            cacheUserSession(loggedInUser, authToken);

            observer.authSuccess(loggedInUser, authToken);
        }
    }

    // RegisterHandler
    private class RegisterHandler extends BackgroundTaskHandler<AuthServiceObserver> {

        public RegisterHandler(AuthServiceObserver registerObserver) {
            super(registerObserver);
        }

        @Override
        protected void handleSuccessMessage(AuthServiceObserver observer, Bundle data) {
            User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);

            cacheUserSession(registeredUser, authToken);

            observer.authSuccess(registeredUser, authToken);
        }
    }

    public interface GetUserObserver extends ServiceTemplate.ServiceObserver {
        void getUserSucceeded(User user);
    }

    public void getUser(String userAlias, GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(getUserObserver));
        execute(getUserTask);
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

    private void cacheUserSession(User user, AuthToken authToken) {
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);
    }

}
