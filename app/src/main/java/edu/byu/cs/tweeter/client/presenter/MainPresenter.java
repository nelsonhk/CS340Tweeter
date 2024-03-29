package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {

    private StatusService statusService;

    public MainPresenter(MainView mainView) {
        super(mainView);
    }

    public interface MainView extends Presenter.View {
        void displayInfoToast(String message);
        void removeInfoToast();
        void logout();
        void displayFollowButton(boolean isFollower);
        void updateFollowButton(boolean removed);
        void updateFollowingAndFollowersCounts();
        void setFollowerCount(int count);
        void setFollowingCount(int count);
    }

    public StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public void getFollowingCount(User selectedUser, Executor executor) {
        new FollowService().getFollowingCount(selectedUser, new GetFollowingCountObserver(), executor);
    }

    private class GetFollowingCountObserver implements FollowService.GetFollowingCountObserver {

        @Override
        public void getFollowingCountSuccess(int followingCount) {
            ((MainView) view).setFollowingCount(followingCount);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }
    }


    public void getFollowerCount(User selectedUser, Executor executor) {
        new FollowService().getFollowerCount(selectedUser, new GetFollowerCountObserver(), executor);
    }

    private class GetFollowerCountObserver implements FollowService.GetFollowerCountObserver {

        @Override
        public void getFollowerCountSuccess(int count) {
            ((MainView) view).setFollowerCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }
    }

    public void postStatus(String post) {

        ((MainView) view).displayInfoToast("Posting Status...");

        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            getStatusService().postStatus(newStatus, new PostStatusObserver());
        } catch (Exception ex) {
            view.displayErrorMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    public class PostStatusObserver implements StatusService.PostStatusObserver {

        @Override
        public void postStatusSuccess() {
            ((MainView) view).removeInfoToast();
            ((MainView) view).displayInfoToast("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }
    }

    public void follow(User selectedUser) {
        new FollowService().follow(selectedUser, new FollowObserver());
    }

    private class FollowObserver implements FollowService.FollowObserver {

        @Override
        public void followSuccess() {
            ((MainView) view).updateFollowingAndFollowersCounts();
            ((MainView) view).updateFollowButton(false);
            ((MainView) view).displayInfoToast("Adding ");
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }
    }

    public void logout() {
        new UserService().logout(new LogoutObserver());
    }

    private class LogoutObserver implements UserService.LogoutObserver {

        @Override
        public void logoutSuccess() {
            ((MainView) view).displayInfoToast("Logging Out...");
            ((MainView) view).removeInfoToast();
            ((MainView) view).logout();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }
    }

    /*
    Unfollow Background functionality
     */

    public void unfollow(User selectedUser) {
        new FollowService().unfollow(selectedUser, new UnfollowObserver());
    }

    private class UnfollowObserver implements FollowService.UnfollowObserver {

        @Override
        public void unfollowSuccess() {
            ((MainView) view).updateFollowingAndFollowersCounts();
            ((MainView) view).updateFollowButton(true);
            ((MainView) view).displayInfoToast("Removing ");
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }
    }

    /*
    IsFollowing functionality (determines whether current logged in user is follower of selected user)
     */

    // tests whether logged in user is following the selectedUser, displays Follow Button accordingly
    public void getIsFollowing(User user) {
        new FollowService().getIsFollowing(user, new GetIsFollowingObserver());
    }

    private class GetIsFollowingObserver implements FollowService.GetIsFollowingObserver {

        @Override
        public void getIsFollowingSuccess(boolean isFollowing) {
            ((MainView) view).displayFollowButton(isFollowing);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage(message);
        }
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

}
