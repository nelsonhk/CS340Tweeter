package edu.byu.cs.tweeter.client.presenter;

import android.widget.ImageView;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthPresenter {

    public LoginPresenter(AuthView loginView) {
        super(loginView);
    }

    @Override
    public String validateInputs(String firstName, String lastName, String username,
                                 String password, ImageView imageToUpload) {
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
    public void authSuccess(String firstName, String lastName, String username, String password, ImageView imageToUpload) {
        new UserService().login(username, password, this);
    }

    @Override
    public String getInfoMessage() {
        return "Logging in...";
    }

}
