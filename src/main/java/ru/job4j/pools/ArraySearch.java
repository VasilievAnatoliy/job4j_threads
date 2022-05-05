package ru.job4j.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Реализация параллельного поиска индекса в массиве объектов.
 * В целях оптимизации, если размер массива не больше 10, используется обычный линейный поиск.
 */

public class ArraySearch extends RecursiveTask<Integer> {
    private final int[] array;
    private final int from;
    private final int to;
    private final int find;

    public ArraySearch(int[] array, int from, int to, int find) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.find = find;
    }

    @Override
    protected Integer compute() {
        if ((to - from) < 10) {
            for (int i = from; i <= to; i++) {
                if (array[i] == find) {
                    return i;
                }
            }
            return -1;
        }
        int mid = (from + to) / 2;
        ArraySearch leftSearch = new ArraySearch(array, from, mid, find);
        ArraySearch rightSearch = new ArraySearch(array, mid + 1, to, find);
        leftSearch.fork();
        rightSearch.fork();
        int left = leftSearch.join();
        int right = rightSearch.join();
        return Math.max(left, right);
    }

    public static int search(int[] array, int find) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ArraySearch(array, 0, array.length - 1, find));
    }
}
