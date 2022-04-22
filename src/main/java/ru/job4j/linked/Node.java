package ru.job4j.linked;

/**
 * Immutable
 * 1. Все поля отмечены final.
 * 2. Состояние объекта не изменяется после создания объекта.
 * 3. Запретить наследование final class.
 * @param <T>
 */
public final class Node<T> {
    private final Node<T> next;
    private final T value;

    public Node(Node<T> next, T value) {
        this.next = next;
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public T getValue() {
        return value;
    }
}
