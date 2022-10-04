package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
//        void handleFailure(String message);
    }

    public void getFollowingCount(User selectedUser, GetFollowingCountObserver getFollowingCountObserver,
                                  Executor executor) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(getFollowingCountObserver));
        executor.execute(followingCountTask);
    }

    private class GetFollowingCountHandler extends BackgroundTaskHandler<GetFollowingCountObserver> {

//        private final GetFollowingCountObserver getFollowingCountObserver;

        public GetFollowingCountHandler(GetFollowingCountObserver getFollowingCountObserver) {
            super(getFollowingCountObserver);
//            this.getFollowingCountObserver = getFollowingCountObserver;
        }

        @Override
        protected void handleSuccessMessage(GetFollowingCountObserver observer, Bundle data) {
            int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
            observer.getFollowingCountSuccess(count);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
//            if (success) {
//                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
//                getFollowingCountObserver.getFollowingCountSuccess(count);
//            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
//                getFollowingCountObserver.handleFailure("Failed to get following count: " + message);
//            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
//                getFollowingCountObserver.handleFailure("Failed to get following count because of exception: " + ex.getMessage());
//            }
//        }

    }

    public interface GetFollowerCountObserver extends ServiceTemplate.ServiceObserver {
        void getFollowerCountSuccess(int followerCount);
//        void handleFailure(String message);
    }

    public void getFollowerCount(User selectedUser, GetFollowerCountObserver getFollowerCountObserver,
                                 Executor executor) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(getFollowerCountObserver));
        executor.execute(followersCountTask);
    }

    private class GetFollowersCountHandler extends BackgroundTaskHandler<GetFollowerCountObserver> {

//        private final GetFollowerCountObserver getFollowerCountObserver;

        public GetFollowersCountHandler(GetFollowerCountObserver getFollowerCountObserver) {
            super(getFollowerCountObserver);
//            this.getFollowerCountObserver = getFollowerCountObserver;
        }

        @Override
        protected void handleSuccessMessage(GetFollowerCountObserver observer, Bundle data) {
            int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
            observer.getFollowerCountSuccess(count);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
//            if (success) {
//                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
//                getFollowerCountObserver.getFollowerCountSuccess(count);
//            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
//                getFollowerCountObserver.handleFailure("Failed to get followers count: " + message);
//            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
//                getFollowerCountObserver.handleFailure("Failed to get followers count because of exception: " + ex.getMessage());
//            }
//        }

    }

    public interface FollowObserver extends ServiceTemplate.ServiceObserver {
        void followSuccess();
//        void handleFailure(String message);
    }

    public void follow(User selectedUser, FollowObserver followObserver) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(followObserver));
        execute(followTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(followTask);
    }

    private class FollowHandler extends BackgroundTaskHandler<FollowObserver> {

//        private final FollowObserver followObserver;
//        private final User selectedUser;

        public FollowHandler(FollowObserver followObserver) {
            super(followObserver);
//            this.followObserver = followObserver;
//            this.selectedUser = selectedUser;
        }

        @Override
        protected void handleSuccessMessage(FollowObserver observer, Bundle data) {
            observer.followSuccess();
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
//            if (success) {
//                followObserver.followSuccess(selectedUser.getName());
//            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
//                followObserver.handleFailure("Failed to follow: " + message);
//            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
//                followObserver.handleFailure("Failed to follow because of exception: " + ex.getMessage());
//            }
//        }
    }

    public interface UnfollowObserver extends ServiceTemplate.ServiceObserver {
        void unfollowSuccess();
//        void handleFailure(String message);
    }

    public void unfollow(User selectedUser, UnfollowObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(unfollowObserver));
        execute(unfollowTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(unfollowTask);
    }

    private class UnfollowHandler extends BackgroundTaskHandler<UnfollowObserver> {

//        private final UnfollowObserver unfollowObserver;
//        private final User selectedUser;

        public UnfollowHandler(UnfollowObserver unfollowObserver) {
            super(unfollowObserver);
//            this.unfollowObserver = unfollowObserver;
//            this.selectedUser = selectedUser;
        }

        @Override
        protected void handleSuccessMessage(UnfollowObserver observer, Bundle data) {
            observer.unfollowSuccess();
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
//            if (success) {
//                unfollowObserver.unfollowSuccess();
//            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
//                unfollowObserver.handleFailure("Failed to unfollow: " + message);
//            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
//                unfollowObserver.handleFailure("Failed to unfollow because of exception: " + ex.getMessage());
//            }
//        }

    }

    public interface GetIsFollowingObserver extends ServiceTemplate.ServiceObserver {
        void getIsFollowingSuccess(boolean isFollowing);
//        void handleFailure(String message);
    }

    // tests whether logged in user is following the selectedUser
    public void getIsFollowing(User selectedUser, GetIsFollowingObserver getIsFollowingObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(getIsFollowingObserver));
        execute(isFollowerTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(isFollowerTask);
    }

    private class IsFollowerHandler extends BackgroundTaskHandler<GetIsFollowingObserver> {

//        private final GetIsFollowingObserver getIsFollowingObserver;

        public IsFollowerHandler(GetIsFollowingObserver getIsFollowingObserver) {
            super(getIsFollowingObserver);
//            this.getIsFollowingObserver = getIsFollowingObserver;
        }

        @Override
        protected void handleSuccessMessage(GetIsFollowingObserver observer, Bundle data) {
            boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            observer.getIsFollowingSuccess(isFollower);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
//            if (success) {
//                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
//
//                // If logged in user if a follower of the selected user, display the follow button as "following"
//                getIsFollowingObserver.getIsFollowingSuccess(isFollower);
//            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
//                getIsFollowingObserver.handleFailure("Failed to determine following relationship: " + message);
//            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
//                getIsFollowingObserver.handleFailure("Failed to determine following relationship because of exception: " + ex.getMessage());
//            }
//        }
    }

    public interface GetFollowingObserver extends ServiceTemplate.ServiceObserver {
        void getFollowingSuccess(List<User> following, boolean hasMorePages);
//        void handleFailure(String message);
    }

    public void loadMoreFollowing(AuthToken authToken, User user, int PAGE_SIZE, User lastFollowing,
                                  GetFollowingObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(authToken, user, PAGE_SIZE,
                lastFollowing, new GetFollowingHandler(getFollowingObserver));
        execute(getFollowingTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getFollowingTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends BackgroundTaskHandler<GetFollowingObserver> {

//        private final GetFollowingObserver getFollowingObserver;

        public GetFollowingHandler(GetFollowingObserver getFollowingObserver) {
            super(getFollowingObserver);
//            this.getFollowingObserver = getFollowingObserver;
        }

        @Override
        protected void handleSuccessMessage(GetFollowingObserver observer, Bundle data) {
            List<User> following = (List<User>) data.getSerializable(GetFollowingTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
            observer.getFollowingSuccess(following, hasMorePages);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
//            if (success) {
//                List<User> following = (List<User>) msg.getData().getSerializable(GetFollowingTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
//                getFollowingObserver.getFollowingSuccess(following, hasMorePages);
//            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
//                getFollowingObserver.handleFailure("Failed to get following: " + message);
//            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
//                getFollowingObserver.handleFailure("Failed to get following because of exception: " + ex.getMessage());
//            }
//        }

    }

    public interface GetFollowersObserver extends ServiceTemplate.ServiceObserver {
        void getFollowersSuccess(List<User> followers, boolean hasMorePages);
//        void handleFailure(String message);
    }

    public void loadMoreFollowers(AuthToken authToken, User user, int PAGE_SIZE, User lastFollower,
                                  GetFollowersObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, user, PAGE_SIZE,
                lastFollower, new GetFollowersHandler(getFollowersObserver));
        execute(getFollowersTask);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends BackgroundTaskHandler<GetFollowersObserver> {

//        GetFollowersObserver getFollowersObserver;

        GetFollowersHandler(GetFollowersObserver getFollowersObserver) {
            super(getFollowersObserver);
//            this.getFollowersObserver = getFollowersObserver;
        }

        @Override
        protected void handleSuccessMessage(GetFollowersObserver observer, Bundle data) {
            List<User> followers = (List<User>) data.getSerializable(GetFollowersTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFollowersTask.MORE_PAGES_KEY);
            observer.getFollowersSuccess(followers, hasMorePages);
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
//            if (success) {
//                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
//                getFollowersObserver.getFollowersSuccess(followers, hasMorePages);
//            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
//                getFollowersObserver.handleFailure("Failed to get followers: " + message);
//            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
//                getFollowersObserver.handleFailure("Failed to get followers because of exception: " + ex.getMessage());
//            }
//        }
//
    }

}
