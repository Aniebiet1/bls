package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HomePage extends JFrame {
    public HomePage() {
        setTitle("Book Lending System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = java.awt.GridBagConstraints.CENTER;

        // header centered
        JLabel hdr = new JLabel("Book Lending System");
        hdr.setFont(hdr.getFont().deriveFont(20f));
        c.gridwidth = 1;
        p.add(hdr, c);

        // buttons column in center
        c.gridy++;
        c.anchor = java.awt.GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 60;

        JButton add = new JButton("Add Book");
        add.addActionListener(ev -> new AddBookPage(this).setVisible(true));
        p.add(add, c);

        c.gridy++;
        JButton lend = new JButton("Lend Book");
        lend.addActionListener(ev -> new LendBookPage(this).setVisible(true));
        p.add(lend, c);

        c.gridy++;
        JButton ret = new JButton("Return Book");
        ret.addActionListener(ev -> new ReturnBookPage(this).setVisible(true));
        p.add(ret, c);

        c.gridy++;
        JButton overdue = new JButton("Overdue List");
        overdue.addActionListener(ev -> new OverduePage(this).setVisible(true));
        p.add(overdue, c);

        c.gridy++;
        JButton all = new JButton("See All Books");
        all.addActionListener(ev -> new AllBooksPage(this).setVisible(true));
        p.add(all, c);

        add(p);
    }
}
