package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends AuthPresenter {

    @Override
    public void authSuccess(String firstName, String lastName, String username,
                            String password, ImageView imageToUpload) {
        String imageBytesBase64 = convertImage(imageToUpload);

        new UserService().register(firstName, lastName, username, password,
                imageBytesBase64, this);
    }

    @Override
    public String getInfoMessage() {
        return "Registering...";
    }

    public RegisterPresenter(AuthView registerView) {
        super(registerView);
    }

    @Override
    public String validateInputs(String firstName, String lastName, String username,
                                     String password, ImageView imageToUpload) {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (username.length() == 0) {
            return "Alias cannot be empty.";
        }
        if (username.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (username.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }

        if (imageToUpload.getDrawable() == null) {
            return "Profile image must be uploaded.";
        }

        return null;
    }

    private String convertImage(ImageView imageToUpload) {
        // Convert image to byte array.
        Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap(); //Presenter
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.

        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
