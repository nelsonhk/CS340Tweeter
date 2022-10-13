package edu.byu.cs.tweeter.client.model.service;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;

public abstract class ServiceTemplate {

    public static interface ServiceObserver {
        void handleFailure(String message);
    }

    public static interface PagedServiceObserver extends ServiceObserver {
        void getItemsSuccess(List items, boolean hasMorePages);
    }

    public <T extends BackgroundTask> void execute(T task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

}