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
public class FollowingPresenter extends PagedPresenter<User> {

    /**
     * Creates an instance.
     *
     * @param view      the view for which this class is the presenter.
     */
    public FollowingPresenter(FollowingView view) {
        super(view);
//        this.view = view;
    }

    @Override
    public void createService() {
        new FollowService().loadMoreFollowing(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastItem, new PagedPresenterObserver());
    }

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface FollowingView extends PagedPresenter.PagedView<User> {}

}