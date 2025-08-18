package dao;

import model.BorrowedBook;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBookDAO {
    public int lendBook(int bookId, String borrowerName) throws SQLException {
        String insert = "INSERT INTO borrowed_books(book_id, borrower_name, borrow_date, due_date) VALUES(?,?,?,?)";
        LocalDate now = LocalDate.now();
        LocalDate due = now.plusDays(14);
        try (Connection c = DBConnection.get()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, bookId);
                ps.setString(2, borrowerName);
                ps.setDate(3, Date.valueOf(now));
                ps.setDate(4, Date.valueOf(due));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    int generatedId = -1;
                    if (rs.next()) generatedId = rs.getInt(1);

                    // decrement available_quantity
                    try (PreparedStatement ps2 = c.prepareStatement("UPDATE books SET available_quantity = available_quantity - 1 WHERE id = ? AND available_quantity > 0")) {
                        ps2.setInt(1, bookId);
                        int updated = ps2.executeUpdate();
                        if (updated == 0) {
                            c.rollback();
                            throw new SQLException("No available copies to lend");
                        }
                    }

                    c.commit();
                    return generatedId;
                }
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void returnBook(int borrowedId) throws SQLException {
        String sql = "UPDATE borrowed_books SET return_date = ? WHERE id = ? AND return_date IS NULL";
        try (Connection c = DBConnection.get()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, borrowedId);
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    c.rollback();
                    throw new SQLException("No such borrowed record or already returned");
                }

                // increment available quantity for the book
                try (PreparedStatement ps2 = c.prepareStatement("UPDATE books SET available_quantity = available_quantity + 1 WHERE id = (SELECT book_id FROM borrowed_books WHERE id = ?)") ) {
                    ps2.setInt(1, borrowedId);
                    ps2.executeUpdate();
                }

                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<BorrowedBook> listBorrowedBooks() throws SQLException {
        String sql = "SELECT id, book_id, borrower_name, borrow_date, due_date, return_date FROM borrowed_books ORDER BY borrow_date DESC";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<BorrowedBook> out = new ArrayList<>();
            while (rs.next()) {
                BorrowedBook b = new BorrowedBook(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("borrower_name"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("return_date") == null ? null : rs.getDate("return_date").toLocalDate()
                );
                out.add(b);
            }
            return out;
        }
    }

    public List<BorrowedBook> listOverdueBooks() throws SQLException {
        String sql = "SELECT id, book_id, borrower_name, borrow_date, due_date, return_date FROM borrowed_books WHERE return_date IS NULL AND due_date < CURRENT_DATE ORDER BY due_date";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<BorrowedBook> out = new ArrayList<>();
            while (rs.next()) {
                BorrowedBook b = new BorrowedBook(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("borrower_name"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        null
                );
                out.add(b);
            }
            return out;
        }
    }
}
