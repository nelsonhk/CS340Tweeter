package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        //        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(logoutTask);
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
//        void handleFailure(String message);
    }

    public interface RegisterObserver extends ServiceTemplate.ServiceObserver {
        void registerSucceeded(User user, AuthToken authToken);
//        void handleFailure(String message);
    }

    public interface GetUserObserver extends ServiceTemplate.ServiceObserver {
        void getUserSucceeded(User user);
//        void handleFailure(String message);
    }

    public void getUser(String userAlias, GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(getUserObserver));
        execute(getUserTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getUserTask);
    }

    public void login(String username, String password, LoginObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(loginObserver));
        execute(loginTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(loginTask);
    }

    public void register(String firstName, String lastName, String username,
                         String password, String imageBytesBase64, RegisterObserver registerObserver) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                username, password, imageBytesBase64, new RegisterHandler(registerObserver));
        execute(registerTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(registerTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends BackgroundTaskHandler<LoginObserver> {

//        private final LoginObserver loginObserver;

        public LoginHandler(LoginObserver loginObserver) {
            super(loginObserver);
//            this.loginObserver = loginObserver;
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

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
//            if (success) {
//                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
//                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);
//
//                // Cache user session information
//                Cache.getInstance().setCurrUser(loggedInUser);
//                Cache.getInstance().setCurrUserAuthToken(authToken);
//
//                loginObserver.loginSucceeded(loggedInUser, authToken);
//
//            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
//                loginObserver.handleFailure("Failed to login: " + message);
//            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
//                loginObserver.handleFailure("Failed to login because of exception: " + ex.getMessage());
//            }
//        }
    }

    // RegisterHandler
    private class RegisterHandler extends BackgroundTaskHandler<RegisterObserver> {

//        private final RegisterObserver registerObserver;

        public RegisterHandler(RegisterObserver registerObserver) {
            super(registerObserver);
//            this.registerObserver = registerObserver;
        }

        @Override
        protected void handleSuccessMessage(RegisterObserver observer, Bundle data) {
            User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);

            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            observer.registerSucceeded(registeredUser, authToken);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
//            if (success) {
//                User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
//                AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
//
//                Cache.getInstance().setCurrUser(registeredUser);
//                Cache.getInstance().setCurrUserAuthToken(authToken);
//
//                registerObserver.registerSucceeded(registeredUser, authToken);
//
//            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
//                registerObserver.handleFailure("Failed to register: " + message);
//            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
//                String message = ex.getMessage();
//                registerObserver.handleFailure("Failed to register because of exception: " + message);
//            }
//        }
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends BackgroundTaskHandler<GetUserObserver> {

//        private final GetUserObserver getUserObserver;

        public GetUserHandler(GetUserObserver getUserObserver) {
            super(getUserObserver);
//            this.getUserObserver = getUserObserver;
        }

        @Override
        protected void handleSuccessMessage(GetUserObserver observer, Bundle data) {
            User user = (User) data.getSerializable(GetUserTask.USER_KEY);
            observer.getUserSucceeded(user);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
//            if (success) {
//                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
//                getUserObserver.getUserSucceeded(user);
//            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
//                getUserObserver.handleFailure("Failed to get user's profile: " + message);
//            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
//                getUserObserver.handleFailure("Failed to get user's profile because of exception: " + ex.getMessage());
//            }
//        }

    }

}
