package ap2025.hw4;

import java.util.Scanner;

public class ShotDown implements Runnable {

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            scanner.nextLine();
        }
        SchedulerMain.shotDown();
    }
}
