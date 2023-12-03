package Methods;

import java.sql.*;

public class Transaction{
    private static final String dbname = "BookStore";
    private static final String user = "postgres";
    private static final String pass = "fidan123";

    public Connection connect_to_db() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }

    public void placeOrder(int customerId, int bookId, int quantity) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement updateBookStmt = null;

        try {
            conn = connect_to_db();
            conn.setAutoCommit(false); // Start transaction

            // Check book availability
            int availableQuantity = getAvailableQuantity(conn, bookId);
            if (availableQuantity >= quantity) {
                // Insert into Orders table
                orderStmt = conn.prepareStatement(
                        "INSERT INTO orders (order_date, total_amount, customer_id) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);

                java.util.Date utilDate = new java.util.Date();
                java.sql.Timestamp orderDate = new java.sql.Timestamp(utilDate.getTime());
                orderStmt.setTimestamp(1, orderDate);
                orderStmt.setDouble(2, calculateTotalAmount(conn, bookId, quantity));
                orderStmt.setInt(3, customerId);
                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                int orderId = -1;
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                }

                // Update Books table
                updateBookStmt = conn.prepareStatement(
                        "UPDATE books SET stock_quantity = stock_quantity - ? WHERE book_id = ?");
                updateBookStmt.setInt(1, quantity);
                updateBookStmt.setInt(2, bookId);
                updateBookStmt.executeUpdate();

                conn.commit(); // Commit transaction
                System.out.println("Order placed successfully. Order ID: " + orderId);
            } else {
                System.out.println("Not enough quantity available for the book.");
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on failure
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (orderStmt != null) {
                    orderStmt.close();
                }
                if (updateBookStmt != null) {
                    updateBookStmt.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getAvailableQuantity(Connection conn, int bookId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT stock_quantity FROM books WHERE book_id = ?");
        stmt.setInt(1, bookId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("stock_quantity");
        }
        return 0;
    }

    private double calculateTotalAmount(Connection conn, int bookId, int quantity) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT price FROM books WHERE book_id = ?");
        stmt.setInt(1, bookId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            double price = rs.getDouble("price");
            return price * quantity;
        }
        return 0.0;
    }
}
