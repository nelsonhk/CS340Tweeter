package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {

    private static final int PAGE_SIZE = 10;
    private final StoryView view;
    private Status lastStatus;
    private static final String LOG_TAG = "StoryPresenter";
    private boolean hasMorePages;
    private boolean isLoading = false;

    public StoryPresenter(StoryView view) {
        this.view = view;
    }

    public interface StoryView extends PagedPresenter.PagedView<Status> {
//        void displayInfoMessage();
//        void addItems(List<Status> statuses);
//        void displayErrorMessage(String message);
//        void setLoadingFooter(boolean isLoading);
//        void startUserActivity(User user);
    }

    public void getStory(User user) {
        setLoading(true);
        view.setLoadingFooter(true);

        new StatusService().getStory(user, PAGE_SIZE, lastStatus, new GetStoryObserver());
    }

    private class GetStoryObserver implements StatusService.GetStoryObserver {

        @Override
        public void getItemsSuccess(List items, boolean hasMorePages) {
            setLoading(false);
            view.setLoadingFooter(false);

            setLastStatus((items.size() > 0) ? (Status) items.get(items.size() - 1) : null);
            setHasMorePages(hasMorePages);

            view.addItems(items);
        }

//        @Override
//        public void getStorySuccess(List<Status> statuses, boolean hasMorePages) {
//            setLoading(false);
//            view.setLoadingFooter(false);
//
//            setLastStatus((statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null);
//            setHasMorePages(hasMorePages);
//
//            view.addItems(statuses);
//        }

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            view.setLoadingFooter(false);

            String errorMessage = message;
            Log.e(LOG_TAG, errorMessage);

            view.displayErrorMessage(errorMessage);
        }

    }

//    public void getUser(String username) {
//        new UserService().getUser(username, new GetUserObserver());
//    }
//
//    private class GetUserObserver implements UserService.GetUserObserver {
//
//        @Override
//        public void getUserSucceeded(User user) {
//            view.startUserActivity(user);
//            view.displayInfoMessage();
//        }
//
//        @Override
//        public void handleFailure(String message) {
//            view.displayErrorMessage(message);
//        }
//    }


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
