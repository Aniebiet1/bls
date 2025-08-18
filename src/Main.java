import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.SwingUtilities;

import ui.HomePage;

public class Main {
    public static void main(String[] args) {
        // ensure backups dir exists
        try {
            Files.createDirectories(Path.of("backups"));
        } catch (IOException e) {
            System.err.println("Failed to create backups directory: " + e.getMessage());
        }

        // ensure DB tables exist; if fails, show detailed dialog
        boolean initOk = true;
        try {
            initOk = util.DBInit.ensureTables();
        } catch (Throwable t) {
            initOk = false;
            util.DBInit.lastError = t.getMessage();
        }

        boolean finalInitOk = initOk;
        SwingUtilities.invokeLater(() -> {
            HomePage h = new HomePage();
            h.setVisible(true);
            if (!finalInitOk) {
                new util.DBInitDialog(h, util.DBInit.lastError).setVisible(true);
            }
        });
    }
}
