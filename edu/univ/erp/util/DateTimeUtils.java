package edu.univ.erp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static String now() {
        System.out.println("[DateTimeUtils] Fetching current timestamp...");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println("[DateTimeUtils] Generated timestamp → " + timestamp);
        return timestamp;
    }
}
