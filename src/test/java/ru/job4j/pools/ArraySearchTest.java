package ru.job4j.pools;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static ru.job4j.pools.ArraySearch.search;

public class ArraySearchTest {

    @Test
    public void array() {
        Integer[] array = new Integer[]{8, 15, 7, 3, 4, 42, 0};
        assertEquals(search(array, 0), 6);
    }

    @Test
    public void whenArrayBig() {
        Integer[] array = new Integer[]{1, 71, 2, 4, 3, 52, 54, 4, 5, 1, 18, 6, 7, 8, 9, 10};
        assertEquals(search(array, 7), 12);
    }

    @Test
    public void whenArrayBigThenSearchLast() {
        Integer[] array = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        assertEquals(search(array, 12), 12);
    }
}