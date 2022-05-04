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
        return memory.computeIfPresent(stored.getId(), (k, v) -> {
            if (v.getVersion() != model.getVersion()) {
                throw new OptimisticException("Versions are not equal");
            }
            return new Base(v.getId(), v.getVersion() + 1);
        }) != null;
    }

    public void delete(Base model) {
        memory.remove(model.getId(), model);
    }

    public Map<Integer, Base> getMemory() {
        return Map.copyOf(memory);
    }
}
