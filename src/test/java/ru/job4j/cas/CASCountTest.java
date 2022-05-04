package ru.job4j.cas;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
public class CASCountTest {

    @Test
    public void increment() throws InterruptedException {
        CASCount count = new CASCount();
        Thread thread1 = new Thread(count::increment);
        Thread thread2 = new Thread(count::increment);
        Thread thread3 = new Thread(count::increment);
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();
        assertThat(count.get(), is(3));
    }
}