package br.com.leonardoz.features.synchronizers;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * Barriers are used for blocking a group of threads until they come together at
 * a single point in order to proceed. Basically, convergence of threads.
 * <p>
 * Accepts a runnable in it's constructor to be called when the threads reach the
 * barrier, but before its unblocked
 * <p>
 * Most common implementation is cyclic barrier.
 */
public class UsingBarriers {

    private static final int parties = 10;

    public static void main(String[] args) {

        Runnable afterPerformBarrier = () -> System.out.println("Well done");

        var executor = Executors.newCachedThreadPool();
        var barrier = new CyclicBarrier(parties, afterPerformBarrier);
        Runnable task = () -> {

            try {
                System.out.println(Thread.currentThread().getName() + "started!");
                Thread.sleep(new Random(1000).nextInt(1222) * 10);
                System.out.println(Thread.currentThread().getName() + "ended!");
                barrier.await();
            } catch (Exception e) {
                System.err.println("Exception!");
            }
        };

        for (int i = 0; i < parties; i++) {
            executor.execute(task);
        }
    }

    private static void doAction() {
        Runnable barrierAction = () -> System.out.println("Well done, guys!");

        var executor = Executors.newCachedThreadPool();
        var barrier = new CyclicBarrier(5, barrierAction);

        Runnable task = () -> {
            try {
                // simulating a task that can take at most 1sec to run
                System.out.println("Doing task for " + Thread.currentThread().getName());
                Thread.sleep(new Random().nextInt(10) * 100);
                System.out.println("Done for " + Thread.currentThread().getName());
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < parties; i++) {
            executor.execute(task);
        }
        executor.shutdown();
    }

}
