package ap2025.hw4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static ap2025.hw4.Priority.*;
import static java.util.Arrays.asList;

public class SchedulerMain {

    // TODO: define fields as you see fit
    public static final Object globalTaskNotificationLock = new Object();
    public static final int queueSize = 10;
    public static ArrayList<TaskConsumer> taskConsumers = new ArrayList<>();
    public static ArrayList<TaskProducer> taskProducers = new ArrayList<>();

    public static void main(String[] args) {
        // TODO: Test and simulate the TaskScheduler

        BlockingTaskQueue priorityLOW = new BlockingTaskQueue(queueSize);
        BlockingTaskQueue priorityMEDIUM = new BlockingTaskQueue(queueSize);
        BlockingTaskQueue priorityHigh = new BlockingTaskQueue(queueSize);

        HashMap<Priority, BlockingTaskQueue> queueMap = new HashMap<>();
        queueMap.put(LOW,priorityLOW);
        queueMap.put(MEDIUM,priorityMEDIUM);
        queueMap.put(HIGH,priorityHigh);

        TaskConsumer taskConsumer01 = new TaskConsumer(queueMap,1001);
        TaskConsumer taskConsumer02 = new TaskConsumer(queueMap,1002);
        TaskConsumer taskConsumer03 = new TaskConsumer(queueMap,1003);

        taskConsumers.addAll( asList(taskConsumer01 , taskConsumer02 , taskConsumer03));

        TaskProducer taskProducer01 = new TaskProducer(queueMap,2001 , 30);
        TaskProducer taskProducer02 = new TaskProducer(queueMap,2002 , 30);
        taskProducers.addAll(asList(taskProducer01,taskProducer02));

        Thread c1 = new Thread(taskConsumer01, "Consumer-1001");
        Thread c2 = new Thread(taskConsumer02, "Consumer-1002");
        Thread c3 = new Thread(taskConsumer03, "Consumer-1003");

        Thread p1 = new Thread(taskProducer01, "Producer-2001");
        Thread p2 = new Thread(taskProducer02, "Producer-2002");

        ShotDown sh = new ShotDown();
        Thread t1 = new Thread(sh);
        t1.setDaemon(true);

        t1.start();




        c1.start();
        c2.start();
        c3.start();
        p1.start();
        p2.start();

        try {
            p1.join();
            p2.join();

            shotDown();

            c1.join();
            c2.join();
            c3.join();




        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    public  static void shotDown(){
            System.out.println("shoting down");
        for(TaskProducer producer : taskProducers){
            producer.requestShutdown();
        }

        for (TaskConsumer consumer : taskConsumers) {
                consumer.signalShutdown();

        }

    }


}
