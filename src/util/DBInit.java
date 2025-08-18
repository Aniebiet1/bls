package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInit {
    // lastError holds the last initialization error message (null on success)
    public static String lastError = null;

    public static final String CREATE_SQL;
    static {
        CREATE_SQL = "-- CREATE TABLE statements for Book Lending System\n"
                + "CREATE TABLE IF NOT EXISTS books (\n"
                + "  id SERIAL PRIMARY KEY,\n"
                + "  title VARCHAR(200) NOT NULL,\n"
                + "  author VARCHAR(200),\n"
                + "  isbn VARCHAR(50) UNIQUE NOT NULL,\n"
                + "  quantity INT NOT NULL,\n"
                + "  available_quantity INT NOT NULL\n"
                + ");\n\n"
                + "CREATE TABLE IF NOT EXISTS borrowed_books (\n"
                + "  id SERIAL PRIMARY KEY,\n"
                + "  book_id INT REFERENCES books(id) ON DELETE CASCADE,\n"
                + "  borrower_name VARCHAR(200) NOT NULL,\n"
                + "  borrow_date DATE NOT NULL,\n"
                + "  due_date DATE NOT NULL,\n"
                + "  return_date DATE\n"
                + ");\n";
    }

    /**
     * Ensure DB tables exist. Returns true on success, false on failure and sets
     * lastError.
     */
    public static boolean ensureTables() {
        lastError = null;
        try (Connection c = DBConnection.get(); Statement s = c.createStatement()) {
            s.execute(CREATE_SQL);
            return true;
        } catch (SQLException e) {
            lastError = e.getMessage();
            System.err.println("Failed to ensure DB tables: " + lastError);
            return false;
        }
    }
}
