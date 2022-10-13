package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

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
    }

    public void loadMoreItems() {};
    public void getUser(String username) {};

    public abstract void getItems();
    public abstract String getDescription();

}
