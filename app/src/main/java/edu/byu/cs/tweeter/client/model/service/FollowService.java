package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface GetFollowersObserver {
        void getFollowersSuccess(List<User> followers, boolean hasMorePages);
        void getFollowersFailed(String message);
    }

    public void loadMoreFollowers(AuthToken authToken, User user, int PAGE_SIZE, User lastFollower,
                                  GetFollowersObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(authToken, user, PAGE_SIZE,
                lastFollower, new GetFollowersHandler(getFollowersObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }



    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {

        GetFollowersObserver getFollowersObserver;

        GetFollowersHandler(GetFollowersObserver getFollowersObserver) {
            this.getFollowersObserver = getFollowersObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);

                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);

                getFollowersObserver.getFollowersSuccess(followers, hasMorePages);

            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                getFollowersObserver.getFollowersFailed("Failed to get followers: " + message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                getFollowersObserver.getFollowersFailed("Failed to get followers because of exception: " + ex.getMessage());
            }
        }
    }

}
