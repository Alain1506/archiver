package com.javarush.task.task31.task3110;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {

    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        String text = bufferedReader.readLine();
        return text;
    }

    public static int readInt() throws IOException {
        String text = readString();
        return Integer.parseInt(text.trim());
    }
}
