package edu.univ.erp.util;

public class PDFReporter {

    public static void export(String content, String filename) {
        System.out.println("[PDFReporter] Export request received.");
        System.out.println("[PDFReporter] Target filename → " + filename);

        if (content == null || content.isBlank()) {
            System.err.println("[PDFReporter] ERROR → Content is empty. PDF will not be generated.");
            return;
        }

        System.out.println("[PDFReporter] Content length → " + content.length() + " characters");
        System.out.println("[PDFReporter] Simulating PDF generation...");

        try {
            Thread.sleep(500); // Simulated processing time
        } catch (InterruptedException ignored) {}

        System.out.println("[PDFReporter] PDF generated successfully → " + filename);
        System.out.println("[PDFReporter] Task complete.");
    }
}
