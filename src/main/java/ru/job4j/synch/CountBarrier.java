package ru.job4j.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;


/**
 * Класс блокирует выполнение по условию счетчика.
 */
@ThreadSafe
public class CountBarrier {
    @GuardedBy("this")
    private final Object monitor = this;
    /**
     * Переменная total содержит количество вызовов метода count().
     */
    private final int total;
    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    /**
     * Метод count изменяет состояние программы. Вызываем метод notifyAll.
     */
    public void count() {
        synchronized (monitor) {
            count++;
            monitor.notifyAll();
        }
    }

    /**
     * Нити, которые выполняют метод await, могут начать работу если поле count >= total.
     * Если оно не равно, то нить переводится в состояние wait.
     */
    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
