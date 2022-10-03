package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {

    private final FeedView feedView;
    private static final int PAGE_SIZE = 10;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private static final String LOG_TAG = "FeedPresenter";

    public FeedPresenter(FeedView feedView) {
        this.feedView = feedView;
    }

    public interface FeedView {
        void startUserActivity(User user);
        void displayInfoMessage();
        void displayErrorMessage(String message);

        void addItems(List<Status> statuses);
        void setLoadingFooter(boolean isLoading);

    }

    public void getFeed(User user) {
        setLoading(true);
        feedView.setLoadingFooter(true);

        new StatusService().getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                lastStatus, new GetFeedObserver());
    }

    private class GetFeedObserver implements StatusService.GetFeedObserver {

        @Override
        public void getFeedSuccess(List<Status> statuses, boolean hasMorePages) {
            setLoading(false);
            feedView.setLoadingFooter(false);

            setLastStatus((statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null);
            setHasMorePages(hasMorePages);

            feedView.addItems(statuses);
        }

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            feedView.setLoadingFooter(false);

            Log.e(LOG_TAG, message);

            feedView.displayErrorMessage(message);
        }
    }


    public void getUser(String username) {
        new UserService().getUser(username, new GetUserObserver());
    }

    private class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void getUserSucceeded(User user) {
            feedView.startUserActivity(user);
            feedView.displayInfoMessage();
        }

        @Override
        public void handleFailure(String message) {
            feedView.displayErrorMessage(message);
        }
    }


    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
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
