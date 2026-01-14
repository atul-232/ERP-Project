package edu.univ.erp.util;

import edu.univ.erp.domain.Course;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CSVExporter {

    public static void exportTranscript(Map<Course, String> transcriptData, String filePath) throws IOException {
        System.out.println("[CSVExporter] Export started → " + filePath);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("CourseCode,CourseTitle,Credits,FinalGrade");
            int count = 0;

            for (Map.Entry<Course, String> entry : transcriptData.entrySet()) {
                Course course = entry.getKey();
                String finalGrade = entry.getValue();

                String line = String.join(",",
                        escapeCsv(course.getCourseCode()),
                        escapeCsv(course.getTitle()),
                        String.valueOf(course.getCredits()),
                        escapeCsv(finalGrade)
                );

                writer.println(line);
                count++;

                System.out.println("[CSVExporter] Wrote → " + course.getCourseCode() + " | Grade: " + finalGrade);
            }

            System.out.println("[CSVExporter] Export completed. Total rows written: " + count);
        } catch (IOException e) {
            System.err.println("[CSVExporter] ERROR while exporting: " + e.getMessage());
            throw e;
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
