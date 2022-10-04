package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends ServiceTemplate {


    public interface PostStatusObserver extends ServiceTemplate.ServiceObserver {
        void postStatusSuccess();
//        void handleFailure(String message);
    }

    public void postStatus(Status newStatus, PostStatusObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusHandler(postStatusObserver));
        execute(statusTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(statusTask);
    }

    // PostStatusHandler

    private class PostStatusHandler extends BackgroundTaskHandler<PostStatusObserver> {

//        private final PostStatusObserver postStatusObserver;

        public PostStatusHandler(PostStatusObserver postStatusObserver) {
            super(postStatusObserver);
//            this.postStatusObserver = postStatusObserver;
        }

        @Override
        protected void handleSuccessMessage(PostStatusObserver observer, Bundle data) {
            observer.postStatusSuccess();
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
//            if (success) {
//                postStatusObserver.postStatusSuccess();
//            } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
//                postStatusObserver.handleFailure("Failed to post status: " + message);
//            } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
//                postStatusObserver.handleFailure("Failed to post status because of exception: " + ex.getMessage());
//            }
//        }

    }

    public interface GetStoryObserver extends ServiceTemplate.ServiceObserver {
        void getStorySuccess(List<Status> statuses, boolean hasMorePages);
//        void handleFailure(String message);
    }

    public void getStory(User user, int PAGE_SIZE, Status lastStatus,
                         GetStoryObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetStoryHandler(getStoryObserver));
        execute(getStoryTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends BackgroundTaskHandler<GetStoryObserver> {

//        private final GetStoryObserver getStoryObserver;

        public GetStoryHandler(GetStoryObserver getStoryObserver) {
            super(getStoryObserver);
//            this.getStoryObserver = getStoryObserver;
        }

        @Override
        protected void handleSuccessMessage(GetStoryObserver observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(GetStoryTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetStoryTask.MORE_PAGES_KEY);
            observer.getStorySuccess(statuses, hasMorePages);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
//            if (success) {
//                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
//                getStoryObserver.getStorySuccess(statuses, hasMorePages);
//            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
//                getStoryObserver.handleFailure("Failed to get story: " + message);
//            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
//                getStoryObserver.handleFailure("Failed to get story because of exception: " + ex.getMessage());
//            }
//        }
    }

    public interface GetFeedObserver extends ServiceTemplate.ServiceObserver {
        void getFeedSuccess(List<Status> statuses, boolean hasMorePages);
//        void handleFailure(String message);
    }

    public void getFeed(AuthToken authToken, User user, int PAGE_SIZE, Status lastStatus,
                        GetFeedObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, PAGE_SIZE,
                lastStatus, new GetFeedHandler(getFeedObserver));
        execute(getFeedTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends BackgroundTaskHandler<GetFeedObserver> {

//        private final GetFeedObserver getFeedObserver;

        public GetFeedHandler(GetFeedObserver getFeedObserver) {
            super(getFeedObserver);
//            this.getFeedObserver = getFeedObserver;
        }

        @Override
        protected void handleSuccessMessage(GetFeedObserver observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
            observer.getFeedSuccess(statuses, hasMorePages);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
//            if (success) {
//                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
//                getFeedObserver.getFeedSuccess(statuses, hasMorePages);
//            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
//                getFeedObserver.handleFailure("Failed to get feed: " + message);
//            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
//                getFeedObserver.handleFailure("Failed to get feed because of exception: " + ex.getMessage());
//            }
//        }

    }

}
