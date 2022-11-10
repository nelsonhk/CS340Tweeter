package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.ServiceTemplate;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;

public abstract class PagedBackgroundTaskHandler<T extends ServiceTemplate.PagedServiceObserver>
        extends BackgroundTaskHandler<T> {


    public PagedBackgroundTaskHandler(ServiceTemplate.ServiceObserver observer) {
        super((T) observer);
    }


    @Override
    protected void handleSuccessMessage(ServiceTemplate.PagedServiceObserver observer, Bundle data) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.getItemsSuccess(items, hasMorePages);
    }

}
