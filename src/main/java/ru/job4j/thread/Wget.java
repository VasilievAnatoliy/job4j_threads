package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Консольная программа
 * скачивает файл из сети с ограничением по скорости скачки(количество MB в секунду)
 * На вход 2 аргумента: URL и количество MB в секунду.
 * Если скорость скачивания быстрей указанной, то выставляется пауза за счет Thread.sleep.
 * Пауза вычисляться из указанной скорости на скачку MB и прошедшего времени.
 */

public class Wget implements Runnable {
    private static final int MB = 1048576;
    private final String url;
    private final int speed;
    private String file;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }


    @Override
    public void run() {
        getFileName();
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] dateBuffer = new byte[1024];
            int downloadData = 0;
            int bytesRead;
            long startTime = System.currentTimeMillis();
            while ((bytesRead = in.read(dateBuffer, 0, 1024)) != -1) {
                downloadData += bytesRead;
                fileOutputStream.write(dateBuffer, 0, bytesRead);
                long timeWork = System.currentTimeMillis() - startTime;
                if (downloadData >= (speed * MB)) {
                    if (timeWork < 1000) {
                        Thread.sleep(1000 - timeWork);
                    }
                    downloadData = 0;
                    startTime = System.currentTimeMillis();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getFileName() {
        try {
            file = String.valueOf(Paths.get(new URL(url).getPath()).getFileName());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args URL и количество MB в секунду.
     */
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "need 2 args: URL and number of downloaded MB per second");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
