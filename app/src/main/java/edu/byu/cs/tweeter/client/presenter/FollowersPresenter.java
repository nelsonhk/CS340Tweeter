package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    private static final int PAGE_SIZE = 10;
    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private static final String LOG_TAG = "FollowersPresenter";

    public void setLastFollower(User lastFollower) {
        this.lastFollower = lastFollower;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public interface FollowersView {
        void addItems(List<User> newUsers);
        void displayErrorMessage(String message);
        void setLoadingFooter(boolean isLoading);
        void startUserActivity(User user);
        void displayInfoMessage();
    }

    FollowersView followersView;

    public FollowersPresenter(FollowersView followersView) {
        this.followersView = followersView;
    }

    public void loadMoreFollowers(User user) {
        setLoading(true);
        followersView.setLoadingFooter(true);

        new FollowService().loadMoreFollowers(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
    }

    public void getUser(String username) {
        new UserService().getUser(username, new GetUserObserver());
    }


    private class GetFollowersObserver implements FollowService.GetFollowersObserver {

        @Override
        public void getFollowersSuccess(List<User> followers, boolean hasMorePages) {
            setLoading(false);
            followersView.setLoadingFooter(false);

            setLastFollower((followers.size() > 0) ? followers.get(followers.size() - 1) : null);
            setHasMorePages(hasMorePages);

            followersView.addItems(followers);
        }

        @Override
        public void getFollowersFailed(String message) {
            setLoading(false);
            followersView.setLoadingFooter(false);

            String errorMessage = message;
            Log.e(LOG_TAG, errorMessage);

            followersView.displayErrorMessage(errorMessage);
        }
    }

    private class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void getUserSucceeded(User user) {
            followersView.startUserActivity(user);
            followersView.displayInfoMessage();
        }

        @Override
        public void getUserFailed(String message) {
            followersView.displayErrorMessage(message);
        }
    }

}