package ru.job4j.jcip;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Классическая задача по переводу денег с одного счета на другой в многопоточной среде.
 * Чтобы операции были атомарны, нужен один объект монитора.
 * Объект монитора будет объект класса UserStorage.
 *
 * Структура данных для хранения пользователей UserStorage, потокобезопасна.
 */

@ThreadSafe
public class UserStorage {
    @GuardedBy("this")
    private final ConcurrentMap<Integer, User> users = new ConcurrentHashMap<>();


    public synchronized boolean add(User user) {
        return users.putIfAbsent(user.getId(), user) == null;
    }

    public synchronized boolean update(User user) {
        return users.replace(user.getId(), users.get(user.getId()), user);
    }

    public synchronized boolean delete(User user) {
        return users.remove(user.getId(), user);
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        boolean rsl = false;
        if (!users.containsKey(fromId) || !users.containsKey(toId) || amount <= 0) {
            throw new IllegalArgumentException("data entered incorrectly");
        }

        User from = users.get(fromId);
        User to = users.get(toId);
        if (from.getAmount() >= amount) {
            from.setAmount(from.getAmount() - amount);
            to.setAmount(to.getAmount() + amount);
            rsl = true;
        } else {
            throw new IllegalArgumentException("not enough money in the account");
        }
        return rsl;
    }
}
