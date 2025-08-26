package ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AboutPage extends JFrame {
    public AboutPage(JFrame owner) {
        setTitle("About - Book Lending System");
        setSize(420, 300);
        setLocationRelativeTo(owner);

        // set a different icon for about page (use src/images/about.png on classpath)
        try {
            Image aboutIcon = ImageIO.read(getClass().getResource("/images/about.png"));
            if (aboutIcon != null) {
                setIconImage(aboutIcon);
                // add a small logo at the top of the about page
                JLabel logo = new JLabel(new ImageIcon(aboutIcon.getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
                logo.setHorizontalAlignment(JLabel.CENTER);
                add(logo, BorderLayout.NORTH);
            }
        } catch (IOException | IllegalArgumentException e) {
            // ignore if icon missing
        }

        String aboutText = "Book Lending System\n\n"
            + "Copyright © 2025 Joseph Cosmos. All rights reserved.\n"
            + "Founder: Joseph Cosmos\n\n"
            + "This application is designed to help individuals and small libraries efficiently manage their book collections.\n"
            + "Features include:\n"
            + "- Store and organize book records with detailed metadata\n"
            + "- Lend books to users and track due dates automatically\n"
            + "- View overdue loans and send reminders\n"
            + "- Export backups for data safety and recovery\n"
            + "- Simple, intuitive interface for local usage\n\n"
            + "For support or feedback, please contact josephcosmos743@gmail.com.\n";

        JTextArea ta = new JTextArea(aboutText);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        add(new JScrollPane(ta), BorderLayout.CENTER);
    }
}