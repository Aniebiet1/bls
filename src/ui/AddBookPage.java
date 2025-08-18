package ui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.BookDAO;
import model.Book;

public class AddBookPage extends JDialog {
    private JTextField titleField = new JTextField(30);
    private JTextField authorField = new JTextField(30);
    private JTextField isbnField = new JTextField(20);
    private JTextField qtyField = new JTextField(5);

    public AddBookPage(Frame owner) {
        super(owner, "Add Book", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        p.add(new JLabel("Title:"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        p.add(titleField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("Author:"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        p.add(authorField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("ISBN:"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        p.add(isbnField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("Quantity:"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.4;
        p.add(qtyField, c);

        // reset constraints for button
        c.gridx = 0;
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        JButton save = new JButton("Save");
        save.addActionListener(ev -> onSave());
        p.add(save, c);

        add(p);
    }

    private void onSave() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();
        int qty = 0;
        try {
            qty = Integer.parseInt(qtyField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (title.isEmpty() || isbn.isEmpty() || qty <= 0) {
            JOptionPane.showMessageDialog(this, "Please fill required fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setIsbn(isbn);
        b.setQuantity(qty);
        b.setAvailableQuantity(qty);
        try {
            new BookDAO().addBook(b);
            JOptionPane.showMessageDialog(this, "Book added");
            dispose();
        } catch (SQLException e) {
            String msg = e.getMessage();
            String sqlState = e.getSQLState();
            boolean missingTable = "42P01".equals(sqlState)
                    || (msg != null && msg.contains("relation \"books\" does not exist"));
            if (missingTable) {
                // Attempt to create tables. If create fails (likely permission issue), show
                // DBInitDialog
                boolean created = util.DBInit.ensureTables();
                if (created) {
                    try {
                        new BookDAO().addBook(b);
                        JOptionPane.showMessageDialog(this, "Book added (created missing tables)");
                        dispose();
                        return;
                    } catch (SQLException e2) {
                        JOptionPane.showMessageDialog(this,
                                "Failed to add book after creating tables: " + e2.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    // show DB init dialog with SQL and copy button
                    Frame owner = (Frame) getOwner();
                    new util.DBInitDialog(owner, util.DBInit.lastError).setVisible(true);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Failed to add book: " + msg, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
