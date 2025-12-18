package fr.amvproject.group3.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Debouncer {

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  private ScheduledFuture<?> pendingTask;

  public synchronized void debounce(Runnable task, long delayMillis) {
    if (pendingTask != null && !pendingTask.isDone()) {
      pendingTask.cancel(false);
    }

    pendingTask = scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
  }

  public void shutdown() {
    scheduler.shutdown();
  }
}