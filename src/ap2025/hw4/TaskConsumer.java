package ap2025.hw4;

import java.util.Map;
import java.util.Random;

import static ap2025.hw4.SchedulerMain.globalTaskNotificationLock;

public class TaskConsumer implements Runnable {
    private final Map<Priority, BlockingTaskQueue> priorityQueues;
    private final int workerId;

    public TaskConsumer(Map<Priority, BlockingTaskQueue> priorityQueues, int workerId) {
        this.priorityQueues = priorityQueues;
        this.workerId = workerId;
    }

    volatile boolean shutdownSignalReceived = false;

    @Override
    public void run() {
        System.out.println("Worker " + workerId + " (Thread: " + Thread.currentThread().getName() + ") started.");
        Random random = new Random();

        try {
            while (true) {
                Task task = null;

                // _TO_DO: Implement the core logic for a worker to get a task, respecting priorities and shutdown.
                // start of your implementation

                synchronized (globalTaskNotificationLock) {
                    while (task == null) {
                        task = attemptTakeByPriority();
                        if (task == null) {
                            if (!shutdownSignalReceived) {
                                globalTaskNotificationLock.wait();
                            }else {
                                break;
                            }
                        }
                    }
                    globalTaskNotificationLock.notifyAll();
                }
                if(shutdownSignalReceived && task == null){
                    break;
                }

                // end of your implementation
                // gotten the task and starting to process it now
                System.out.println("Worker " + workerId + " retrieved " + task);
                System.out.println("Worker " + workerId + " is processing " + task);
                Thread.sleep(random.nextInt(401) + 100);
                System.out.println("Worker " + workerId + " finished processing " + task);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Worker " + workerId + " was interrupted. Shutting down.");
        }
        System.out.println("Worker " + workerId + " finished run method.");
    }

    private Task attemptTakeByPriority() {
        Task task = priorityQueues.get(Priority.HIGH).poll();
        if (task != null) return task;

        task = priorityQueues.get(Priority.MEDIUM).poll();
        if (task != null) return task;

        task = priorityQueues.get(Priority.LOW).poll();
        return task;
    }

    private boolean areAllQueuesEmpty() {
        for (Priority p : Priority.values()) {
            if (!priorityQueues.get(p).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void signalShutdown() {
        shutdownSignalReceived = true;
        // _TO_DO: signal shutDown
        // Crucially, notify any workers waiting on the global lock so they can
        // re-evaluate their conditions (especially the shutdown condition).
        synchronized (globalTaskNotificationLock) {
            globalTaskNotificationLock.notifyAll();
        }

    }
}
