package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    public FollowersPresenter(FollowersView followersView) {
        super(followersView);
    }

    public interface FollowersView extends PagedPresenter.PagedView<User> {}

    @Override
    public void createService() {
        new FollowService().loadMoreFollowers(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastItem, new PagedPresenterObserver());
    }

}
