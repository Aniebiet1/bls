package ui;

import dao.BookDAO;
import dao.BorrowedBookDAO;
import model.Book;
import model.BorrowedBook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OverduePage extends JDialog {
    private JTable table;
    private DefaultTableModel model;

    public OverduePage(Frame owner) {
        super(owner, "Overdue Books", true);
        setSize(800, 400);
        setLocationRelativeTo(owner);
        model = new DefaultTableModel(new Object[]{"Borrowed ID", "Book", "Borrower", "Borrow Date", "Due Date"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadOverdue();
    }

    private void loadOverdue() {
        model.setRowCount(0);
        try {
            List<BorrowedBook> list = new BorrowedBookDAO().listOverdueBooks();
            BookDAO bd = new BookDAO();
            for (BorrowedBook bb : list) {
                Book b = bd.findById(bb.getBookId());
                model.addRow(new Object[]{bb.getId(), b == null ? "(unknown)" : b.getTitle(), bb.getBorrowerName(), bb.getBorrowDate(), bb.getDueDate()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load overdue: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
