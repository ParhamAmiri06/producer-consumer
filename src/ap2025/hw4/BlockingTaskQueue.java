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
        // TODO: ap2025.hw4.BlockingTaskQueue put method
        synchronized (this) {
            while (size() >= capacity) {
                this.wait();
            }
            queue.add(task);
        }


    }

    public Task take() throws InterruptedException {
        Task task = null;
        // TODO: ap2025.hw4.BlockingTaskQueue take method (blocking)



        return task;
    }

    public synchronized Task poll() {
        // TODO: ap2025.hw4.BlockingTaskQueue poll method (non-blocking)
        //  should return a ap2025.hw4.Task instead of null
        if(!isEmpty()){
            Task task = queue.removeFirst();
            notifyAll();
            return task;
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
