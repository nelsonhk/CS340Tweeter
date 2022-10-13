package edu.byu.cs.tweeter.client.presenter;

import android.widget.ImageView;

import edu.byu.cs.tweeter.client.model.service.ServiceTemplate;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthPresenter extends Presenter implements ServiceTemplate.AuthServiceObserver {

    public AuthPresenter(AuthView view) {
        super(view);
    }

    public interface AuthView extends View{
        void displayInfoMessage(String message);
        void clearInfoMessage();
        void clearErrorMessage();
        void navigateToUser(User user);
    }

    public void initiateAuth(String firstName, String lastName, String username,
                             String password, ImageView imageToUpload) {
        String message = validateInputs(firstName, lastName, username,
                password, imageToUpload);

        ((AuthView) view).clearErrorMessage();

        if (message == null) {
            ((AuthView) view).displayInfoMessage(getInfoMessage());

            authSuccess(firstName, lastName, username,
                    password, imageToUpload);

        } else {
            ((AuthView) view).clearInfoMessage();
            view.displayErrorMessage(message);
        }
    }

    @Override
    public void authSuccess(User user, AuthToken authToken) {
        ((AuthView) view).clearInfoMessage();
        ((AuthView) view).clearErrorMessage();

        ((AuthView) view).navigateToUser(user);
    }

    @Override
    public void handleFailure(String message) {
        ((AuthView) view).clearInfoMessage();
        ((AuthView) view).clearErrorMessage();

        view.displayErrorMessage(message);
    }

    public abstract String validateInputs(String firstName, String lastName, String username,
                                          String password, ImageView imageToUpload);

    public abstract void authSuccess(String firstName, String lastName, String username,
                                     String password, ImageView imageToUpload);
    public abstract String getInfoMessage();

}
