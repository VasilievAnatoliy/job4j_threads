package ru.job4j.cas;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Чтобы сделать Stack потокобезопасным используем класс concurrent.atomic.AtomicReference
 * Это класс поддерживает CAS операции.
 *
 * Синхронизация блокирует выполнение нитей, то есть программа из многопоточной превращается в однопоточную.
 * Процессоры на уровне ядра поддерживают операцию compare-and-swap. Эта операция атомарная.
 * @param <T>
 */
@ThreadSafe
public class CASStack<T> {

    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    public void push(T value) {
        Node<T> temp = new Node<>(value);
        Node<T> ref;
        do {
            ref = head.get();
            temp.next = ref;
        } while (!head.compareAndSet(ref, temp));
    }

    public T poll() {
        Node<T> ref;
        Node<T> temp;
        do {
            ref = head.get();
            if (ref == null) {
                throw new IllegalStateException("Stack i,s empty");
            }
            temp = ref.next;
        } while (!head.compareAndSet(ref, temp));
        ref.next = null;
        return ref.value;
    }

    private static final class Node<T> {
        final T value;

        Node<T> next;

        public Node(final T value) {
            this.value = value;
        }
    }
}
