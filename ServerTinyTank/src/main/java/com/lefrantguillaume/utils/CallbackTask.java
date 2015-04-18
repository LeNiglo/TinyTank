package com.lefrantguillaume.utils;

/**
 * Created by Styve on 16/03/2015.
 */
public class CallbackTask implements Runnable {
    private final Runnable task;
    private final Callback callback;

    public CallbackTask(Runnable task, Callback callback) {
        this.task = task;
        this.callback = callback;
    }

    public void run() {
        task.run();
        callback.complete();
    }
}
