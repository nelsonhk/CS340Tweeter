package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    private static final int PAGE_SIZE = 10;
    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public PagedPresenter(View view) {
        super(view);
    }

    public interface PagedView<T> extends View {
        public abstract void setLoadingFooter(boolean isLoading);
        public abstract void addItems(List<T> items);
        public abstract void startUserActivity(User user);
        public abstract void displayInfoMessage();
    }

    public void loadMoreItems() {
        //TODO: write method body
    };

    public void getUser(String username) {
        new UserService().getUser(username, new GetUserObserver());
    }

    private class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void getUserSucceeded(User user) {
            ((PagedView<T>) view).startUserActivity(user);
            ((PagedView<T>) view).displayInfoMessage();
        }

        @Override
        public void handleFailure(String message) {
            ((PagedView<T>) view).displayErrorMessage(message);
        }
    }


    public abstract void getItems();
    public abstract String getDescription();

}
