package ru.job4j.cache;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CacheTest {

    @Test
    public void whenAdd3ThenSize3() {
        Cache cache = new Cache();
        assertTrue(cache.add(new Base(1, 1)));
        assertTrue(cache.add(new Base(2, 1)));
        assertTrue(cache.add(new Base(3, 1)));
        assertThat(cache.getMemory().size(), is(3));
    }

    @Test
    public void whenAdd3AndDelete2ThenSize1() {
        Cache cache = new Cache();
        cache.add(new Base(1, 1));
        cache.add(new Base(2, 1));
        cache.add(new Base(3, 1));
        cache.delete(cache.getMemory().get(1));
        cache.delete(cache.getMemory().get(2));
        assertThat(cache.getMemory().size(), is(1));
    }

    @Test
    public void whenUpdateThenIncrementVersion() {
        Cache cache = new Cache();
        cache.add(new Base(1, 1));
        assertTrue(cache.update(cache.getMemory().get(1)));
        assertThat(cache.getMemory().get(1).getVersion(), is(2));
        assertTrue(cache.update(cache.getMemory().get(1)));
        assertThat(cache.getMemory().get(1).getVersion(), is(3));
    }

    @Test(expected = OptimisticException.class)
    public void whenException() {
        Cache cache = new Cache();
        cache.add(new Base(1, 1));
        Base base = new Base(1, 4);
        cache.update(base);
    }

}