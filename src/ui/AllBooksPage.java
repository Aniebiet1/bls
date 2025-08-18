package ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.BookDAO;
import model.Book;

public class AllBooksPage extends JDialog {
    private JTable table;
    private DefaultTableModel model;

    public AllBooksPage(Frame owner) {
        super(owner, "All Books", true);
        setSize(700, 400);
        setLocationRelativeTo(owner);
        model = new DefaultTableModel(new Object[] { "ID", "Title", "Author", "ISBN", "Quantity", "Available" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(ev -> loadBooks());
        bottom.add(refresh);

        JButton del = new JButton("Delete Selected");
        del.addActionListener(ev -> onDeleteSelected());
        bottom.add(del);

        add(bottom, BorderLayout.SOUTH);

        loadBooks();
    }

    private void onDeleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to delete", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int id = (int) model.getValueAt(r, 0);
        int ok = JOptionPane.showConfirmDialog(this, "Delete selected book (id=" + id + ")?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION)
            return;
        try {
            new BookDAO().deleteBook(id);
            JOptionPane.showMessageDialog(this, "Book deleted");
            loadBooks();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBooks() {
        model.setRowCount(0);
        try {
            List<Book> books = new BookDAO().listBooks();
            for (Book b : books) {
                model.addRow(new Object[] { b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.getQuantity(),
                        b.getAvailableQuantity() });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load books: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
