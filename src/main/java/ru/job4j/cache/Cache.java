package ru.job4j.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {
        Base stored = memory.get(model.getId());
        Base rsl = memory.computeIfPresent(stored.getId(), (k, v) -> {
            if (stored.getVersion() != model.getVersion()) {
                throw new OptimisticException("Versions are not equal");
            }
            return new Base(stored.getId(), stored.getVersion() + 1);
        });
        return rsl != null;
    }

    public void delete(Base model) {
        memory.remove(model.getId(), model);
    }

    public Map<Integer, Base> getMemory() {
        return Map.copyOf(memory);
    }

    public static void main(String[] args) {
        Cache cache = new Cache();
        cache.add(new Base(1, 1));
        cache.add(new Base(2, 1));
        cache.add(new Base(3, 1));
        cache.update(cache.memory.get(1));
        System.out.println(cache.memory.get(1));
    }
}
