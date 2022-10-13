package edu.byu.cs.tweeter.client.presenter;

public class Presenter {

    protected View view;

    public Presenter(View view) {
        this.view = view;
    }

    public interface View {
        public abstract void displayErrorMessage(String errorMessage);
    }

}
