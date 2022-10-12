package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PagedBackgroundTaskHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends ServiceTemplate {


    public interface PostStatusObserver extends ServiceTemplate.ServiceObserver {
        void postStatusSuccess();
    }

    public void postStatus(Status newStatus, PostStatusObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusHandler(postStatusObserver));
        execute(statusTask);
    }

    // PostStatusHandler

    private class PostStatusHandler extends BackgroundTaskHandler<PostStatusObserver> {

        public PostStatusHandler(PostStatusObserver postStatusObserver) {
            super(postStatusObserver);
        }

        @Override
        protected void handleSuccessMessage(PostStatusObserver observer, Bundle data) {
            observer.postStatusSuccess();
        }
    }

    public interface GetStoryObserver extends PagedServiceObserver {}

//    public interface GetStoryObserver extends ServiceTemplate.ServiceObserver {
////        void getStorySuccess(List<Status> statuses, boolean hasMorePages);
////        void handleFailure(String message);
//    }

    public void getStory(User user, int PAGE_SIZE, Status lastStatus,
                         GetStoryObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetStoryHandler(getStoryObserver));
        execute(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends PagedBackgroundTaskHandler<GetStoryObserver> {

        public GetStoryHandler(GetStoryObserver getStoryObserver) {
            super(getStoryObserver);
        }

        @Override
        public void callObserver(List items, boolean hasMorePages) {
            observer.getItemsSuccess(items, hasMorePages);
        }

//        @Override
//        protected void handleSuccessMessage(GetStoryObserver observer, Bundle data) {
//            List<Status> statuses = (List<Status>) data.getSerializable(GetStoryTask.ITEMS_KEY);
//            boolean hasMorePages = data.getBoolean(GetStoryTask.MORE_PAGES_KEY);
//            observer.getItemsSuccess(statuses, hasMorePages);
//        }


    }

    public interface GetFeedObserver extends PagedServiceObserver {}

//    public interface GetFeedObserver extends ServiceTemplate.ServiceObserver {
////        void getFeedSuccess(List<Status> statuses, boolean hasMorePages);
//////        void handleFailure(String message);
////    }

    public void getFeed(AuthToken authToken, User user, int PAGE_SIZE, Status lastStatus,
                        GetFeedObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, PAGE_SIZE,
                lastStatus, new GetFeedHandler(getFeedObserver));
        execute(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends PagedBackgroundTaskHandler<GetFeedObserver> {

        public GetFeedHandler(GetFeedObserver getFeedObserver) {
            super(getFeedObserver);
        }

        @Override
        public void callObserver(List items, boolean hasMorePages) {
            observer.getItemsSuccess(items, hasMorePages);
        }

//
//        @Override
//        protected void handleSuccessMessage(GetFeedObserver observer, Bundle data) {
//            List<Status> statuses = (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);
//            boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
//            observer.getItemsSuccess(statuses, hasMorePages);
//        }
    }

}
