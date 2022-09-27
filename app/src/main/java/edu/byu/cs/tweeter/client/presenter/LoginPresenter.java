package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter implements UserService.LoginObserver {

    public interface LoginView {
        void displayInfoMessage(String message);
        void clearInfoMessage();

        void displayErrorMessage(String message);
        void clearErrorMessage();

        void navigateToUser(User user);
    }

    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    public void initiateLogin(String username, String password) {
        String message = validateLogin(username, password);
        if (message == null) {
            loginView.clearErrorMessage();
            loginView.displayInfoMessage("Logging in...");
            new UserService().login(username, password, this);
        } else {
            loginView.clearInfoMessage();
            loginView.displayErrorMessage(message);
        }
    }

    public String validateLogin(String username, String password) {
        if (username.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (username.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return null;
    }

    @Override
    public void loginSucceeded(User user, AuthToken authToken) {
        loginView.clearInfoMessage();
        loginView.clearErrorMessage();

        loginView.navigateToUser(user);
    }

    @Override
    public void loginFailed(String message) {
        loginView.clearInfoMessage();
        loginView.clearErrorMessage();

        loginView.displayErrorMessage(message);
    }

}
