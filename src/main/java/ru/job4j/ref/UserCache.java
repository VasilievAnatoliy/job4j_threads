package ru.job4j.ref;

import net.jcip.annotations.NotThreadSafe;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Локальные переменные видны только своей нити. Но если мы добавляем локальный объект в общее хранилище,
 * то ссылка на непотокобезопасный объект становится доступна всем.
 *
 * Одно из решений проблемы многопоточной среды - избавление от общих ресурсов.
 * Сделать копию общего ресурса. В этом случае каждая нить работает с локальной копией.
 * Для этого нужно в кеш добавлять копию объекта и возвращать копию.
 */
@NotThreadSafe
public class UserCache {
    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public void add(User user) {
        users.put(id.incrementAndGet(), User.of(user.getName()));

    }

    public User findById(int id) {
        return User.of(users.get(id).getName());
    }

    public List<User> findAll() {
        return users.values()
                .stream()
                .map(x -> User.of(x.getName()))
                .collect(Collectors.toList());
    }
}


