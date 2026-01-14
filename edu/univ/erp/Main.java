package edu.univ.erp;

import com.formdev.flatlaf.FlatLightLaf;
import edu.univ.erp.ui.auth.LoginWindow;
import edu.univ.erp.util.DBInitializer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("[MAIN] University ERP Application Starting...");

        System.out.println("[MAIN] Applying UI theme...");
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            System.out.println("[MAIN] UI Theme set to FlatLaf Light.");
        } catch (Exception ex) {
            System.err.println("[MAIN] Failed to initialize FlatLaf. Using system default theme.");
        }

        System.out.println("[MAIN] Initializing databases...");
        long dbStart = System.currentTimeMillis();
        DBInitializer.initializeAuthDB();
        DBInitializer.initializeErpDB();
        System.out.println("[MAIN] Database initialization completed in " +
                (System.currentTimeMillis() - dbStart) + " ms");

        System.out.println("[MAIN] Launching Login Window...");
        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
            System.out.println("[MAIN] Login Window is now visible.");
        });

        System.out.println("[MAIN] Startup sequence completed.");
    }
}
