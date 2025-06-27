package ap2025.hw4;

import java.util.Map;
import java.util.Random;


public class Canceler {
    private static final Random  RANDOM= new Random();

    public static void cancel(Task task){
        double chance = RANDOM.nextDouble();
        double dynamicFalseProbability = 0.10 + (RANDOM.nextDouble() * (0.15 - 0.10));
        if(chance <= dynamicFalseProbability){
            task.cancel();
        }
    }

}
