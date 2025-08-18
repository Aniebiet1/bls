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

public class ReturnBookPage extends JDialog {
    private JTable table;
    private DefaultTableModel model;

    public ReturnBookPage(Frame owner) {
        super(owner, "Return Book", true);
        setSize(700, 400);
        setLocationRelativeTo(owner);
        model = new DefaultTableModel(new Object[]{"Borrowed ID", "Book", "Borrower", "Borrow Date", "Due Date", "Returned"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JButton ret = new JButton("Return Selected");
        ret.addActionListener(ev -> onReturn());
        add(ret, BorderLayout.SOUTH);

        loadBorrowed();
    }

    private void loadBorrowed() {
        model.setRowCount(0);
        try {
            List<BorrowedBook> list = new BorrowedBookDAO().listBorrowedBooks();
            BookDAO bd = new BookDAO();
            for (BorrowedBook bb : list) {
                Book b = bd.findById(bb.getBookId());
                model.addRow(new Object[]{bb.getId(), b == null ? "(unknown)" : b.getTitle(), bb.getBorrowerName(), bb.getBorrowDate(), bb.getDueDate(), bb.getReturnDate()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load borrowed list: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onReturn() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a borrowed row", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int borrowedId = (int) model.getValueAt(r, 0);
        Object retVal = model.getValueAt(r, 5);
        if (retVal != null) {
            JOptionPane.showMessageDialog(this, "Already returned", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            new BorrowedBookDAO().returnBook(borrowedId);
            JOptionPane.showMessageDialog(this, "Book returned");
            loadBorrowed();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to return: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
