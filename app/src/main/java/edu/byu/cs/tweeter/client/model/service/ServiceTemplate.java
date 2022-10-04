package edu.byu.cs.tweeter.client.model.service;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.model.domain.Status;

public abstract class ServiceTemplate {

    public static interface ServiceObserver {
        void handleFailure(String message);
    //    void handleException(Exception exception);
    }

    public static interface PagedServiceObserver extends ServiceObserver {
        void getItemsSuccess(List items, boolean hasMorePages);
    }

    public <T extends BackgroundTask> void execute(T task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

}


//public abstract class ServiceTemplate <T extends BackgroundTask, S extends ServiceObserver> {
//
//    T backgroundTask;
//
//    public ServiceTemplate() {
//        this.backgroundTask = backgroundTask;
//    }
//
//    public void execute(ServiceObserver observer) {
//        backgroundTask = getBackgroundTask(observer);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(backgroundTask);
//    }
//
//    public abstract T getBackgroundTask(ServiceObserver observer);
//
//}
