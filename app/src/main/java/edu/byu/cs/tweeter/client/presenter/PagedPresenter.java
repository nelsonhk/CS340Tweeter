package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.ServiceTemplate;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    public abstract void createService();

    protected User user;
    protected static final int PAGE_SIZE = 10;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading = false;

    public PagedPresenter(View view) {
        super(view);
    }

    public interface PagedView<T> extends View {
        public abstract void setLoadingFooter(boolean isLoading);
        public abstract void addItems(List<T> items);
        public abstract void startUserActivity(User user);
        public abstract void displayInfoMessage();
    }

    public void loadMoreItems(User user) {
        setLoading(true);
        ((PagedView) view).setLoadingFooter(true);
        createService();
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

    public class PagedPresenterObserver implements ServiceTemplate.PagedServiceObserver {

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            ((PagedView) view).setLoadingFooter(false);
            String errorMessage = message;
            view.displayErrorMessage(errorMessage);
        }

        @Override
        public void getItemsSuccess(List items, boolean hasMorePages) {
            setLoading(false);
            ((PagedView) view).setLoadingFooter(false);

            setLastItem((items.size() > 0) ? (T) items.get(items.size() - 1) : null);
            setHasMorePages(hasMorePages);

            ((PagedView) view).addItems(items);
        }
    }

    public void setLastItem(T lastItem) {
        this.lastItem = lastItem;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

}
