package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class Presenter {

    protected View view;

    public Presenter(View view) {
        this.view = view;
    }

    public interface View {
        public abstract void displayErrorMessage(String errorMessage);
    }

}
