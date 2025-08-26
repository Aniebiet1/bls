package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class HomePage extends JFrame {
    public HomePage() {
        setTitle("LibraMate"); // Changed title to "LibraMate"
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // try to set app icon (use src/images/home.png on classpath)
        try {
            Image appIcon = ImageIO.read(getClass().getResource("/images/home.png"));
            if (appIcon != null) setIconImage(appIcon);
        } catch (IOException | IllegalArgumentException e) { /* ignore */ }

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(14, 14, 14, 14);

        // Header centered across the top but only above the right two columns
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2; // cover separator + right area
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        JLabel hdr = new JLabel("Book Lending System");
        hdr.setHorizontalAlignment(JLabel.CENTER);
        hdr.setFont(hdr.getFont().deriveFont(Font.BOLD, 22f));
        hdr.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        p.add(hdr, c);

        // Content row: left column (blue panel) | separator | right column (buttons)
        c.gridwidth = 1;
        c.gridy = 1;

        // Left blue filled panel - full height (span header row too)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(30, 144, 255)); // DodgerBlue
        leftPanel.setOpaque(true);
        leftPanel.setPreferredSize(new Dimension(260, 0)); // fixed width, flexible height
        leftPanel.setBorder(BorderFactory.createEmptyBorder(24, 20, 20, 20));

        // add logo to the top of left panel using images/home.png
        try {
            Image homeLogo = ImageIO.read(getClass().getResource("/images/home.png"));
            if (homeLogo != null) {
                JLabel logoLbl = new JLabel(new ImageIcon(homeLogo.getScaledInstance(72, 72, Image.SCALE_SMOOTH)));
                JPanel logoWrap = new JPanel();
                logoWrap.setOpaque(false);
                logoWrap.add(logoLbl);
                leftPanel.add(logoWrap, BorderLayout.NORTH);
            }
        } catch (IOException | IllegalArgumentException ex) {
            // ignore if logo missing
        }

        // white multi-line heading like the sketch
        JLabel leftText = new JLabel(
                "<html><div style='color:white; font-size:26px; font-weight:700; line-height:1.05;'>Never Worry<br/>About<br/>Managing<br/>Your<br/>Library</div></html>");
        leftText.setHorizontalAlignment(JLabel.LEFT);
        leftText.setFont(leftText.getFont().deriveFont(Font.BOLD, 26f));
        leftPanel.add(leftText, BorderLayout.CENTER);

        // About link placed at the bottom of left panel (white link)
        JLabel aboutLink = new JLabel("<html><a href='#' style='color:white;'>About this app</a></html>");
        aboutLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AboutPage(HomePage.this).setVisible(true);
            }
        });
        JPanel aboutWrap = new JPanel();
        aboutWrap.setOpaque(false);
        aboutWrap.add(aboutLink);
        leftPanel.add(aboutWrap, BorderLayout.SOUTH);

        c.gridx = 0;
        c.gridy = 0;           // start at top so it spans the header vertically
        c.gridheight = 2;      // span header + content rows
        c.weightx = 0;
        c.weighty = 1; // let left panel stretch vertically across the window
        c.fill = GridBagConstraints.BOTH;
        p.add(leftPanel, c);

        // vertical separator (thin, full height)
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(2, 0));
        sep.setBackground(new Color(220, 220, 220));
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.BOTH;
        p.add(sep, c);

        // Right area -- buttons in a 2x2 grid that expand and look professional
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        GridBagConstraints btnC = new GridBagConstraints();
        btnC.insets = new Insets(12, 18, 12, 18);
        btnC.fill = GridBagConstraints.BOTH;
        btnC.weightx = 1;
        btnC.weighty = 1;

        // First row
        btnC.gridx = 0;
        btnC.gridy = 0;
        JButton add = new JButton("Add Book");
        styleMainButton(add);
        add.addActionListener(ev -> new AddBookPage(this).setVisible(true));
        rightPanel.add(add, btnC);

        btnC.gridx = 1;
        JButton lend = new JButton("Lend Book");
        styleMainButton(lend);
        lend.addActionListener(ev -> new LendBookPage(this).setVisible(true));
        rightPanel.add(lend, btnC);

        // Second row
        btnC.gridx = 0;
        btnC.gridy = 1;
        JButton ret = new JButton("Return Book");
        styleMainButton(ret);
        ret.addActionListener(ev -> new ReturnBookPage(this).setVisible(true));
        rightPanel.add(ret, btnC);

        btnC.gridx = 1;
        JButton overdue = new JButton("Overdue List");
        styleMainButton(overdue);
        overdue.addActionListener(ev -> new OverduePage(this).setVisible(true));
        rightPanel.add(overdue, btnC);

        // Third row: See All Books button spanning both columns
        btnC.gridx = 0;
        btnC.gridy = 2;
        btnC.gridwidth = 2;
        btnC.weighty = 0.35; // slightly smaller vertical weight for this row
        JButton all = new JButton("See All Books");
        styleSecondaryButton(all);
        all.addActionListener(ev -> new AllBooksPage(this).setVisible(true));
        rightPanel.add(all, btnC);

        // place rightPanel in main layout
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        p.add(rightPanel, c);

        add(p);
    }

    // helper to style main action buttons
    private void styleMainButton(JButton b) {
        b.setFont(b.getFont().deriveFont(Font.BOLD, 18f));
        b.setBackground(new Color(50, 50, 50));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        b.setOpaque(true);
        // remove fixed preferred size to allow buttons to expand
        // b.setPreferredSize(new Dimension(220, 90));
    }

    // helper to style the secondary full-width button
    private void styleSecondaryButton(JButton b) {
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 16f));
        b.setBackground(new Color(50, 50, 50));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        b.setOpaque(true);
        // prefer flexible height so it fills available width
        // b.setPreferredSize(new Dimension(0, 64));
    }
}
