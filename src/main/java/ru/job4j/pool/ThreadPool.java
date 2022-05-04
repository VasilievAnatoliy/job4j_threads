package ru.job4j.pool;

import ru.job4j.synch.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    private final int size = Runtime.getRuntime().availableProcessors();
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(size);

    public void pool() {
        for (int i = 0; i < size; i++) {
            threads.add(new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        tasks.poll().run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }));
        }
        threads.forEach(Thread::start);
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool();
        threadPool.pool();
        AtomicInteger integer = new AtomicInteger();
        for (int i = 0; i < 20; i++) {
            threadPool.work(() -> System.out.println(integer.incrementAndGet()));
        }
        threadPool.shutdown();
    }
}
