package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    public FeedPresenter(FeedView feedView) {
        super(feedView);
    }

    public interface FeedView extends PagedPresenter.PagedView<Status> {}

    @Override
    public void createService() {
        new StatusService().getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                lastItem, new PagedPresenterObserver());
    }

}
