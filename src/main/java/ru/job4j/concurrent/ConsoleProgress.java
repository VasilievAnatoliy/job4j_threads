package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000);
        progress.interrupt();

    }

    @Override
    public void run() {
        int index = 0;
        char[] process = new char[]{'\\', '|', '/'};
        while (!Thread.currentThread().isInterrupted()) {
            if (index == process.length) {
                index = 0;
            }
            try {
                System.out.print("\r load: " + process[index++]);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
