package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PagedBackgroundTaskHandler;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter.PagedPresenterObserver;
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

    public void getStory(User user, int PAGE_SIZE, Status lastStatus,
                         PagedPresenterObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new GetStoryHandler(getStoryObserver));
        execute(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends PagedBackgroundTaskHandler<PagedPresenterObserver> {

        public GetStoryHandler(PagedPresenterObserver getStoryObserver) {
            super(getStoryObserver);
        }

        @Override
        public void callObserver(List items, boolean hasMorePages) {
            observer.getItemsSuccess(items, hasMorePages);
        }
    }

    public void getFeed(AuthToken authToken, User user, int PAGE_SIZE, Status lastStatus,
                        PagedPresenterObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, PAGE_SIZE,
                lastStatus, new GetFeedHandler(getFeedObserver));
        execute(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends PagedBackgroundTaskHandler<PagedPresenterObserver> {

        public GetFeedHandler(PagedPresenterObserver getFeedObserver) {
            super(getFeedObserver);
        }

        @Override
        public void callObserver(List items, boolean hasMorePages) {
            observer.getItemsSuccess(items, hasMorePages);
        }
    }

}
