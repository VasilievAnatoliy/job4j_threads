package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public String getContent() {
        return content(x -> true);
    }

    public String getContentWithoutUnicode() {
        return content(x -> x < (0x80));
    }

    public synchronized String content(Predicate<Character> filter) {
        StringBuilder output = new StringBuilder();
        try (InputStream i = new BufferedInputStream(new FileInputStream(file))) {
            int data;
            while ((data = i.read()) != -1) {
                char charData = (char) data;
                if (filter.test(charData)) {
                    output.append(charData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
