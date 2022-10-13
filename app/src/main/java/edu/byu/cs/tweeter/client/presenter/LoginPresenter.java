package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends Presenter implements UserService.LoginObserver {

    public interface LoginView extends Presenter.View {
        void displayInfoMessage(String message);
        void clearInfoMessage();
        void clearErrorMessage();
        void navigateToUser(User user);
    }

    public LoginPresenter(LoginView loginView) {
        super(loginView);
    }

    public void initiateLogin(String username, String password) {
        String message = validateLogin(username, password);
        if (message == null) {
            ((LoginView) view).clearErrorMessage();
            ((LoginView) view).displayInfoMessage("Logging in...");
            new UserService().login(username, password, this);
        } else {
            ((LoginView) view).clearInfoMessage();
            view.displayErrorMessage(message);
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
        ((LoginView) view).clearInfoMessage();
        ((LoginView) view).clearErrorMessage();

        ((LoginView) view).navigateToUser(user);
    }

    @Override
    public void handleFailure(String message) {
        ((LoginView) view).clearInfoMessage();
        ((LoginView) view).clearErrorMessage();

        view.displayErrorMessage(message);
    }

}
