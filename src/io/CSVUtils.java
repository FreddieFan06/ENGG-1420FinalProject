package io;

public class CSVUtils {
    public static String[] parseLine(String line) {
        return line.split(",", -1);
    }
}
