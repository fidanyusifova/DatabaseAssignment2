package Methods;

import objects.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderCrud {
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

    public void deleteOrder(int orderId) {
        try (Connection conn = connect_to_db()) {
            conn.setAutoCommit(false);

            // Delete entries from orderdetail
            deleteOrderDetails(conn, orderId);

            // Update stock quantity in books table
            updateStockQuantityForDeletedOrder(conn, orderId);

            // Delete the order itself
            deleteOrderFromOrders(conn, orderId);

            // Commit the transaction
            conn.commit();
            System.out.println("Order deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete order. Rolling back transaction.");
        }
    }

    private void deleteOrderDetails(Connection conn, int orderId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM orderdetail WHERE order_id = ?")) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        }
    }

    private void updateStockQuantityForDeletedOrder(Connection conn, int orderId) throws SQLException {
        String query = "UPDATE books " +
                "SET stock_quantity = stock_quantity + od.quantity " +
                "FROM orderdetail od " +
                "WHERE books.book_id = od.book_id AND od.order_id = ?";
        System.out.println("SQL Query: " + query);

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        }
    }

    private void deleteOrderFromOrders(Connection conn, int orderId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM orders WHERE order_id = ?")) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        }
    }


    public void updateOrderDetails(int orderId, List<Integer> newBookIds, List<Integer> newQuantities) {
        try (Connection conn = connect_to_db()) {
            conn.setAutoCommit(false);

            // Retrieve existing order details
            List<OrderDetail> existingOrderDetail = getOrderDetailsByOrderId(conn, orderId);

            if (existingOrderDetail != null && !existingOrderDetail.isEmpty()) {
                // Update order details based on the provided lists
                for (int i = 0; i < existingOrderDetail.size(); i++) {
                    int orderDetailId = existingOrderDetail.get(i).getOrderDetailId();
                    int updatedBookId = (i < newBookIds.size()) ? newBookIds.get(i) : existingOrderDetail.get(i).getBookId();
                    int updatedQuantity = (i < newQuantities.size()) ? newQuantities.get(i) : existingOrderDetail.get(i).getQuantity();

                    // Update each order detail
                   // updateOrderDetailInDatabase(conn, orderDetailId, updatedBookId, updatedQuantity);
                }

                // Commit the transaction
                conn.commit();
                System.out.println("Order details updated successfully.");
            } else {
                System.out.println("No order details found for the specified order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update order details. Rolling back transaction.");
        }
    }

    private List<OrderDetail> getOrderDetailsByOrderId(Connection conn, int orderId) throws SQLException {
        List<OrderDetail> orderDetails = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT * FROM orderdetail WHERE order_id = ?")) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail(
                        rs.getInt("order_detail_id"),
                        rs.getInt("order_id"),
                        rs.getInt("book_id"),
                        rs.getInt("quantity")
                );
                orderDetails.add(orderDetail);
            }
        }
        return orderDetails;
    }

}
