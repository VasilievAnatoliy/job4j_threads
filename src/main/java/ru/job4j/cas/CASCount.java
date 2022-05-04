package ru.job4j.cas;

import java.util.concurrent.atomic.AtomicReference;

public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>(0);


    public void increment() {
        int getCount;
        do {
            getCount = get();
        } while (!count.compareAndSet(getCount, getCount + 1));

    }

    public int get() {
        return count.get();
    }
}

