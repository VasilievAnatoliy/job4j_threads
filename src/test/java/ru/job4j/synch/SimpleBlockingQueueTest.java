package ru.job4j.synch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SimpleBlockingQueueTest {

    @Test
    public void addAndRemove() throws InterruptedException {
        var blockingQueue = new SimpleBlockingQueue(2);
        Thread producer = new Thread(
                () -> {
                    for (int i = 1; i <= 5; i++) {
                        blockingQueue.offer(i);
                    }

                }
        );
        List<Integer> list = new ArrayList<>();
        Thread consumer = new Thread(
                () -> {
                    for (int i = 1; i <= 5; i++) {
                        list.add((Integer) blockingQueue.poll());
                    }
                }
        );
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(list, is(List.of(1, 2, 3, 4, 5)));
    }

    @Test
    public void whenSizeLargerThanCapacity() throws InterruptedException {
        var blockingQueue = new SimpleBlockingQueue(2);
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < 5; i++) {
                        blockingQueue.offer(i);
                    }
                }
        );
        producer.start();
        producer.join(100);
        assertThat(Thread.State.WAITING, is(producer.getState()));

    }
}