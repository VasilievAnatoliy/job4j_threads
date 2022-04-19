package ru.job4j.thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Консольная программа
 * скачивает файл из сети с ограничением по скорости скачки.
 * ограничение скорости скачивания на 1024 байт.
 * Если время меньше указанного, то выставляется пауза за счет Thread.sleep.
 * Пауза вычисляться
 */

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed * 1000;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("pom_tmp.xml")) {
            byte[] dateBuffer = new byte[1024];
            int bytesRead;
            long startTime = System.currentTimeMillis();
            while ((bytesRead = in.read(dateBuffer, 0, dateBuffer.length - 1)) != -1) {
                fileOutputStream.write(dateBuffer, 0, bytesRead);
                long timeWork = System.currentTimeMillis() - startTime;
                if (speed > timeWork) {
                    Thread.sleep(speed - timeWork);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
