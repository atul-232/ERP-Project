package edu.univ.erp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageUtils {

    private static String timestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static String caller() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        if (stack.length > 3) {
            return stack[3].getClassName();
        }
        return "UnknownSource";
    }

    public static void info(String msg) {
        System.out.println("[INFO] " + timestamp() + " [" + caller() + "] → " + msg);
    }

    public static void error(String msg) {
        System.err.println("[ERROR] " + timestamp() + " [" + caller() + "] → " + msg);
    }
}
