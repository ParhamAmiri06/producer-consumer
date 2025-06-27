package ap2025.hw4;

import java.util.LinkedList;
import java.util.Queue;

public class CustomBlockingTaskQueue {
    private final int capacity;
    private final Queue<Task> taskQueue;
    private final Queue<Object> waiters;

    public CustomBlockingTaskQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        this.capacity = capacity;
        this.taskQueue = new LinkedList<>();

        this.waiters = new LinkedList<>();
    }

    public synchronized void put(Task task) throws InterruptedException {

        Object waitNode = new Object();
        waiters.add(waitNode);


        while (waiters.peek() != waitNode || taskQueue.size() == this.capacity) {
            wait();
        }

        taskQueue.add(task);


        if (waiters.peek() == waitNode) {
            waiters.poll();
        } else {
            waiters.remove(waitNode);
        }

        notifyAll();

    }

    public synchronized Task take() throws InterruptedException {

        Object waitNode = new Object();
        waiters.add(waitNode);

        Task task;

        while (waiters.peek() != waitNode || taskQueue.isEmpty()) {
            wait();
        }


        task = taskQueue.poll();

        if (waiters.peek() == waitNode) {
            waiters.poll();
        } else {
            waiters.remove(waitNode);
        }


        notifyAll();

        return task;
    }




}
