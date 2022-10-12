package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The presenter for the "following" functionality of the application.
 */
public class FollowingPresenter {

    private static final String LOG_TAG = "FollowingPresenter";
    public static final int PAGE_SIZE = 10;
    private final FollowingView view;
    private User lastFollowee;
    private boolean hasMorePages;
    private boolean isLoading = false;

    /**
     * Creates an instance.
     *
     * @param view      the view for which this class is the presenter.
     */
    public FollowingPresenter(FollowingView view) {
        this.view = view;
    }

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface FollowingView {
        void setLoadingFooter(boolean isLoading);
        void addItems(List<User> newUsers);
        void displayErrorMessage(String message);

        void startUserActivity(User user);
        void displayInfoMessage();
    }

    /**
     * Called by the view to request that another page of "following" users be loaded.
     */
    public void loadMoreFollowing(User user) {
        setLoading(true);
        view.setLoadingFooter(true);

        getFollowing(user, PAGE_SIZE, lastFollowee);
    }

    /**
     * Requests the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned for a previous request. This is an asynchronous
     * operation.
     *
     * @param targetUser   the user for whom followees are being retrieved.
     * @param limit        the maximum number of followees to return.
     * @param lastFollowee the last followee returned in the previous request (can be null).
     */
    public void getFollowing(User targetUser, int limit, User lastFollowee) {
        new FollowService().loadMoreFollowing(Cache.getInstance().getCurrUserAuthToken(),
                targetUser, limit, lastFollowee, new GetFollowingObserver());
    }

//    /**
//     * Returns an instance of {@link FollowService}. Allows mocking of the FollowService class
//     * for testing purposes. All usages of FollowService should get their FollowService
//     * instance from this method to allow for mocking of the instance.
//     *
//     * @return the instance.
//     */
//    public FollowService getFollowingService() {
//        return new FollowService();
//    }

    public void getUser(String username) {
        new UserService().getUser(username, new GetUserObserver());
    }

    private class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void getUserSucceeded(User user) {
            view.startUserActivity(user);
            view.displayInfoMessage();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }
    }

    private class GetFollowingObserver implements FollowService.GetFollowingObserver {
        @Override
        public void getItemsSuccess(List items, boolean hasMorePages) {
            setLoading(false);
            view.setLoadingFooter(false);

            setLastFollowee((items.size() > 0) ? (User) items.get(items.size() - 1) : null);
            setHasMorePages(hasMorePages);

            view.addItems(items);
        }

//        /**
//         * Adds new following retrieved asynchronously from the service to the view.
//         *
//         * @param following    the retrieved following.
//         * @param hasMorePages whether or not there are more following to be retrieved.
//         */
//        @Override
//        public void getFollowingSuccess(List<User> following, boolean hasMorePages) {
//            setLoading(false);
//            view.setLoadingFooter(false);
//
//            setLastFollowee((following.size() > 0) ? following.get(following.size() - 1) : null);
//            setHasMorePages(hasMorePages);
//
//            view.addItems(following);
//        }

        /**
         * Notifies the presenter when asynchronous retrieval of followees failed or when an exception
         * occurred.
         *
         * @param message error message.
         */
        @Override
        public void handleFailure(String message) {
            setLoading(false);
            view.setLoadingFooter(false);

            String errorMessage = message;
            Log.e(LOG_TAG, errorMessage);

            view.displayErrorMessage(errorMessage);
        }

    }

    /**
     * Getters and Setters
     */

    private void setLastFollowee(User lastFollowee) {
        this.lastFollowee = lastFollowee;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    private void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
    }
}