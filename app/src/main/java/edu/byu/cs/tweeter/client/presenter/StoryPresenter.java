package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public StoryPresenter(StoryView view) {
        super(view);
    }

    @Override
    public void createService() {
        new StatusService().getStory(user, PAGE_SIZE, lastItem, new PagedPresenterObserver());
    }

    public interface StoryView extends PagedPresenter.PagedView<Status> {}

}
