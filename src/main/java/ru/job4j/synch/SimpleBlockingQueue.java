package ru.job4j.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Реализация шаблона Producer Consumer.
 * Реализовать собственную версию bounded blocking queue. Это блокирующая очередь, ограниченная по размеру.
 * <p>
 * В задании нельзя использовать потокобезопасные коллекции реализованные в JDK.
 * Задача используя, wait/notify реализовать блокирующую очередь.
 *
 * @param <T>
 */

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private final int capacity;

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Если очередь заполнена полностью, то поток(и) Producer блокируется,
     * до тех пор пока Consumer не извлечет очередные данные.
     *
     * @param value - значение помещаемое в очередь.
     */
    public synchronized void offer(T value) throws InterruptedException {
        while (queue.size() == capacity) {
               wait();
        }
        queue.offer(value);
        notifyAll();
    }

    /**
     * Если очередь пуста поток(и) Consumer блокируется,
     * до тех пор пока Producer не поместит в очередь данные.
     *
     * @return - Удалённое значение из очереди
     */
    public synchronized T poll() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        notifyAll();
        return queue.poll();

    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
