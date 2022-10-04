package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;
import java.util.concurrent.Executor;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends ServiceTemplate {

    public interface GetFollowingCountObserver extends ServiceTemplate.ServiceObserver {
        void getFollowingCountSuccess(int followingCount);
    }

    public void getFollowingCount(User selectedUser, GetFollowingCountObserver getFollowingCountObserver,
                                  Executor executor) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(getFollowingCountObserver));
        executor.execute(followingCountTask);
    }

    private class GetFollowingCountHandler extends BackgroundTaskHandler<GetFollowingCountObserver> {

        public GetFollowingCountHandler(GetFollowingCountObserver getFollowingCountObserver) {
            super(getFollowingCountObserver);
        }

        @Override
        protected void handleSuccessMessage(GetFollowingCountObserver observer, Bundle data) {
            int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
            observer.getFollowingCountSuccess(count);
        }
    }

    public interface GetFollowerCountObserver extends ServiceTemplate.ServiceObserver {
        void getFollowerCountSuccess(int followerCount);
    }

    public void getFollowerCount(User selectedUser, GetFollowerCountObserver getFollowerCountObserver,
                                 Executor executor) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(getFollowerCountObserver));
        executor.execute(followersCountTask);
    }

    private class GetFollowersCountHandler extends BackgroundTaskHandler<GetFollowerCountObserver> {

        public GetFollowersCountHandler(GetFollowerCountObserver getFollowerCountObserver) {
            super(getFollowerCountObserver);
        }

        @Override
        protected void handleSuccessMessage(GetFollowerCountObserver observer, Bundle data) {
            int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
            observer.getFollowerCountSuccess(count);
        }
    }

    public interface FollowObserver extends ServiceTemplate.ServiceObserver {
        void followSuccess();
    }

    public void follow(User selectedUser, FollowObserver followObserver) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(followObserver));
        execute(followTask);
    }

    private class FollowHandler extends BackgroundTaskHandler<FollowObserver> {

        public FollowHandler(FollowObserver followObserver) {
            super(followObserver);
        }

        @Override
        protected void handleSuccessMessage(FollowObserver observer, Bundle data) {
            observer.followSuccess();
        }
    }

    public interface UnfollowObserver extends ServiceTemplate.ServiceObserver {
        void unfollowSuccess();
    }

    public void unfollow(User selectedUser, UnfollowObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(unfollowObserver));
        execute(unfollowTask);
    }

    private class UnfollowHandler extends BackgroundTaskHandler<UnfollowObserver> {

        public UnfollowHandler(UnfollowObserver unfollowObserver) {
            super(unfollowObserver);
        }

        @Override
        protected void handleSuccessMessage(UnfollowObserver observer, Bundle data) {
            observer.unfollowSuccess();
        }
    }

    public interface GetIsFollowingObserver extends ServiceTemplate.ServiceObserver {
        void getIsFollowingSuccess(boolean isFollowing);
    }

    // tests whether logged in user is following the selectedUser
    public void getIsFollowing(User selectedUser, GetIsFollowingObserver getIsFollowingObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(getIsFollowingObserver));
        execute(isFollowerTask);
    }

    private class IsFollowerHandler extends BackgroundTaskHandler<GetIsFollowingObserver> {

        public IsFollowerHandler(GetIsFollowingObserver getIsFollowingObserver) {
            super(getIsFollowingObserver);
        }

        @Override
        protected void handleSuccessMessage(GetIsFollowingObserver observer, Bundle data) {
            boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            observer.getIsFollowingSuccess(isFollower);
        }
    }

    public interface GetFollowingObserver extends ServiceTemplate.ServiceObserver {
        void getFollowingSuccess(List<User> following, boolean hasMorePages);
    }

    public void loadMoreFollowing(AuthToken authToken, User user, int PAGE_SIZE, User lastFollowing,
                                  GetFollowingObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken, user, PAGE_SIZE,
                lastFollowing, new GetFollowingHandler(getFollowingObserver));
        execute(getFollowingTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends BackgroundTaskHandler<GetFollowingObserver> {

        public GetFollowingHandler(GetFollowingObserver getFollowingObserver) {
            super(getFollowingObserver);
        }

        @Override
        protected void handleSuccessMessage(GetFollowingObserver observer, Bundle data) {
            List<User> following = (List<User>) data.getSerializable(GetFollowingTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
            observer.getFollowingSuccess(following, hasMorePages);
        }
    }

    public interface GetFollowersObserver extends ServiceTemplate.ServiceObserver {
        void getFollowersSuccess(List<User> followers, boolean hasMorePages);
    }

    public void loadMoreFollowers(AuthToken authToken, User user, int PAGE_SIZE, User lastFollower,
                                  GetFollowersObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, user, PAGE_SIZE,
                lastFollower, new GetFollowersHandler(getFollowersObserver));
        execute(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends BackgroundTaskHandler<GetFollowersObserver> {

        GetFollowersHandler(GetFollowersObserver getFollowersObserver) {
            super(getFollowersObserver);
        }

        @Override
        protected void handleSuccessMessage(GetFollowersObserver observer, Bundle data) {
            List<User> followers = (List<User>) data.getSerializable(GetFollowersTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFollowersTask.MORE_PAGES_KEY);
            observer.getFollowersSuccess(followers, hasMorePages);
        }
    }

}
