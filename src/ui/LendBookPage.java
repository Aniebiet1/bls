package ui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.BookDAO;
import dao.BorrowedBookDAO;
import model.Book;

public class LendBookPage extends JDialog {
    private JComboBox<Book> booksCombo = new JComboBox<>();
    private JTextField borrowerField = new JTextField(30);

    public LendBookPage(Frame owner) {
        super(owner, "Lend Book", true);
        setSize(500, 200);
        setLocationRelativeTo(owner);
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        p.add(new JLabel("Book:"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        p.add(booksCombo, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("Borrower name:"), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        p.add(borrowerField, c);

        c.gridx = 0;
        c.gridy++;
        JButton lend = new JButton("Lend");
        lend.addActionListener(ev -> onLend());
        p.add(lend, c);

        add(p);
        loadBooks();
    }

    private void loadBooks() {
        try {
            List<Book> books = new BookDAO().listBooks();
            DefaultComboBoxModel<Book> m = new DefaultComboBoxModel<>();
            for (Book b : books) {
                if (b.getAvailableQuantity() > 0)
                    m.addElement(b);
            }
            booksCombo.setModel(m);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load books: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLend() {
        Book b = (Book) booksCombo.getSelectedItem();
        String borrower = borrowerField.getText().trim();
        if (b == null) {
            JOptionPane.showMessageDialog(this, "No book selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (borrower.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Borrower name required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int id = new BorrowedBookDAO().lendBook(b.getId(), borrower);
            JOptionPane.showMessageDialog(this, "Book lent (id=" + id + ")");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to lend book: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
