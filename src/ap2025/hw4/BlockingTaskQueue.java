package ap2025.hw4;

import java.util.LinkedList;
import java.util.List;

import static ap2025.hw4.SchedulerMain.globalTaskNotificationLock;

public class BlockingTaskQueue {
    private final List<Task> queue;
    private final int capacity;


    public BlockingTaskQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive for the queue.");
        }
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public void put(Task task) throws InterruptedException {
        // _TO_DO: ap2025.hw4.BlockingTaskQueue put method
        synchronized (globalTaskNotificationLock) {
            Canceler.cancel(task);
            while (size() >= capacity) {
                globalTaskNotificationLock.wait();
            }
            queue.add(task);
            globalTaskNotificationLock.notifyAll();
        }
    }

    public Task take() throws InterruptedException {
        Task task = null;
        // _TO_DO: ap2025.hw4.BlockingTaskQueue take method (blocking)
        synchronized (globalTaskNotificationLock) {
           while (isEmpty()) {
            globalTaskNotificationLock.wait();
           }
           task = queue.remove(0);
            globalTaskNotificationLock.notifyAll();

        }

        return task;
    }

    public synchronized Task poll() {
        // _TO_DO: ap2025.hw4.BlockingTaskQueue poll method (non-blocking)
        //  should return a ap2025.hw4.Task instead of null
        while (!isEmpty()){
            Task task =  queue.remove(0);
            if (!task.isCancled()) {
                return task;
            }
            System.out.println("the task was cancled");
        }
        return null;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }
}
