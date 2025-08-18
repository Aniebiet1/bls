package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import util.DBConnection;

public class BookDAO {
    public void addBook(Book b) throws SQLException {
        String sql = "INSERT INTO books(title, author, isbn, quantity, available_quantity) VALUES(?,?,?,?,?)";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getAuthor());
            ps.setString(3, b.getIsbn());
            ps.setInt(4, b.getQuantity());
            ps.setInt(5, b.getAvailableQuantity());
            ps.executeUpdate();
        }
    }

    public void updateBook(Book b) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, quantity=?, available_quantity=? WHERE id=?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getAuthor());
            ps.setString(3, b.getIsbn());
            ps.setInt(4, b.getQuantity());
            ps.setInt(5, b.getAvailableQuantity());
            ps.setInt(6, b.getId());
            ps.executeUpdate();
        }
    }

    public List<Book> listBooks() throws SQLException {
        String sql = "SELECT id, title, author, isbn, quantity, available_quantity FROM books ORDER BY title";
        try (Connection c = DBConnection.get();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            List<Book> out = new ArrayList<>();
            while (rs.next()) {
                Book b = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("quantity"),
                        rs.getInt("available_quantity"));
                out.add(b);
            }
            return out;
        }
    }

    public Book findBookByIsbn(String isbn) throws SQLException {
        String sql = "SELECT id, title, author, isbn, quantity, available_quantity FROM books WHERE isbn = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getInt("quantity"),
                            rs.getInt("available_quantity"));
                }
                return null;
            }
        }
    }

    public Book findById(int id) throws SQLException {
        String sql = "SELECT id, title, author, isbn, quantity, available_quantity FROM books WHERE id = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getInt("quantity"),
                            rs.getInt("available_quantity"));
                }
                return null;
            }
        }
    }

    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
