package Methods;

import objects.Author;
import objects.Book;
import objects.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookCrud {
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


    public Author getAuthorById(int authorId) {
        Author author = null;
        try (Connection conn = connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM authors WHERE author_id = ?")) {
            pstmt.setInt(1, authorId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String authorName = rs.getString("author_name");
                    String country = rs.getString("country");
                    // Create an Author object with the retrieved details
                    author = new Author(authorId, authorName, country);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return author;
    }


    //INSERT METHOD
    public void insertBook(Book book) {
        try (Connection conn = connect_to_db()) {
            // Check if the book or its author is null
            if (book == null || book.getAuthor() == null) {
                System.out.println("Book or Author object is null. Book insertion failed.");
                return;
            }


            // Check if the author with the specified authorId exists
            if (!isAuthorExists(conn, book.getAuthor().getAuthorId())) {
                System.out.println("Author with ID " + book.getAuthor().getAuthorId() + " does not exist. Book insertion failed.");
                return;
            }

            // Proceed with inserting the book
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO books (title, genre, price, stock_quantity, author_id) VALUES (?, ?, ?, ?, ?)")) {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getGenre());
                pstmt.setDouble(3, book.getPrice());
                pstmt.setInt(4, book.getStockQuantity());
                pstmt.setInt(5, book.getAuthor().getAuthorId());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Book inserted successfully");
                } else {
                    System.out.println("Failed to insert book");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isAuthorExists(Connection conn, int authorId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT 1 FROM authors WHERE author_id = ?")) {
            pstmt.setInt(1, authorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Retrieve (different versions)
    /*public List<Book> retrieveAllBooks() {
        List<Book> books = new ArrayList<>();

        try (Connection conn = connect_to_db();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");

                // Create a Book object
                Book book = new Book(bookId, title, genre, price, stockQuantity);

                // Add the book to the list
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }*/
    public List<Book> retrieveAllBooks() {
        List<Book> books = new ArrayList<>();

        try (Connection conn = connect_to_db();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT b.*, a.author_name, a.country FROM books b JOIN authors a ON b.author_id = a.author_id")) {

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");

                // Fetch author details
                int authorId = rs.getInt("author_id");
                String authorName = rs.getString("author_name");
                String country = rs.getString("country");
                Author author = new Author(authorId, authorName, country);

                // Create a Book object
                Book book = new Book(bookId ,title, genre, price, stockQuantity, author);

                // Fetch and set orders for this book
                List<Order> orders = retrieveAllOrdersByBookId(bookId);
                book.setOrders(orders);

                // Add the book to the list
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }


    // Retrieve all orders for a given bookId
    public String retrieveBookInfoAndOrdersByBookId(int bookId) {
        Book book = null;
        StringBuilder result = new StringBuilder();

        try (Connection conn = connect_to_db()) {
            // Query to retrieve book information and author details based on book_id
            String query = "SELECT b.book_id, b.title, b.genre, b.price, b.stock_quantity, " +
                    "a.author_id, a.author_name, a.country " +
                    "FROM books b " +
                    "JOIN authors a ON b.author_id = a.author_id " +
                    "WHERE b.book_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, bookId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int retrievedBookId = rs.getInt("book_id");
                        String title = rs.getString("title");
                        String genre = rs.getString("genre");
                        double price = rs.getDouble("price");
                        int stockQuantity = rs.getInt("stock_quantity");

                        int authorId = rs.getInt("author_id");
                        String authorName = rs.getString("author_name");

                        String country = rs.getString("country");

                        Author author = new Author(authorId, authorName, country);

                        book = new Book(bookId, title, genre, price, stockQuantity, author);
                    }
                }
            }

            if (book != null) {
                result.append("Book Details:\n").append(book).append("\n");

                // Retrieve and append all associated orders
                List<Order> orders = retrieveAllOrdersByBookId(bookId);
                if (!orders.isEmpty()) {
                    result.append("Orders:\n");
                    for (Order order : orders) {
                        result.append(order).append("\n");
                    }
                } else {
                    result.append("No orders found for this book.\n");
                }
            } else {
                result.append("Book not found.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private List<Order> retrieveAllOrdersByBookId(int bookId) {
        List<Order> orders = new ArrayList<>();

        try (Connection conn = connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT o.order_id, o.order_date, o.total_amount " +
                             "FROM orderdetail od " +
                             "JOIN orders o ON od.order_id = o.order_id " +
                             "WHERE od.book_id = ?"
             )) {

            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("order_date");
                    LocalDate localOrderDate = (orderDate != null) ? orderDate.toLocalDate() : null;
                    double totalAmount = rs.getDouble("total_amount");

                    // Create an Order object
                    Order order = new Order(orderId, localOrderDate, totalAmount);

                    // Add the order to the list
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    // UPDATE
    public void updateBook(int bookId, String newTitle, String newGenre, double newPrice, int newStockQuantity, int newAuthorId) {
        try (Connection conn = connect_to_db()) {
            // Retrieve the existing book based on the provided bookId
            Book existingBook = findBookById(bookId);

            if (existingBook == null) {
                System.out.println("Book not found");
                return;
            }

            if (!" ".equals(newTitle)) {
                existingBook.setTitle(newTitle);
            }
            if (!" ".equals(newGenre)) {
                existingBook.setGenre(newGenre);
            }
            if (newPrice >= 0) {
                existingBook.setPrice(newPrice);
            }
            if (newStockQuantity >= 0) {
                existingBook.setStockQuantity(newStockQuantity);
            }

            // If a new authorId is provided, update the author of the book
            if (newAuthorId > 0) {
                Author newAuthor = findAuthorById(newAuthorId);
                existingBook.setAuthor(newAuthor);
            }

            // Update the book in the database
            updateBookInDatabase(existingBook);

            System.out.println("Book updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book findBookById(int bookId) throws SQLException {
        try (Connection conn = connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement("SELECT b.*, a.* FROM books b JOIN authors a ON b.author_id = a.author_id WHERE b.book_id = ?")) {
            pstmt.setInt(1, bookId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Populate and return the book object
                    return extractBookFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public void updateBookInDatabase(Book book) throws SQLException {
        try (Connection conn = connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE books SET title = ?, genre = ?, price = ?, stock_quantity = ?, author_id = ? WHERE book_id = ?")) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getGenre());
            pstmt.setDouble(3, book.getPrice());
            pstmt.setInt(4, book.getStockQuantity());
            pstmt.setInt(5, book.getAuthor().getAuthorId());
            pstmt.setInt(6, book.getBookId());

            pstmt.executeUpdate();
        }
    }

    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        int bookId = rs.getInt("book_id");
        String title = rs.getString("title");
        String genre = rs.getString("genre");
        double price = rs.getDouble("price");
        int stockQuantity = rs.getInt("stock_quantity");

        // Extract author attributes and create an Author object
        int authorId = rs.getInt("author_id");
        String authorName = rs.getString("author_name");
        String country = rs.getString("country");
        Author author = new Author(authorId, authorName, country);

        // Create and return the Book object
        return new Book(bookId, title, genre, price, stockQuantity, author);
    }

    public Author findAuthorById(int authorId) throws SQLException {
        try (Connection conn = connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM authors WHERE author_id = ?")) {
            pstmt.setInt(1, authorId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Extract author attributes and create an Author object
                    String authorName = rs.getString("author_name");
                    String country = rs.getString("country");

                    return new Author(authorId, authorName, country);
                }
            }
        }
        return null;
    }


    // DELETE METHOD
    public void deleteBook(int bookId) {
        try (Connection conn = connect_to_db()) {
            conn.setAutoCommit(false);

            // Step 1: Delete entries from order_details
             deleteOrderDetails(conn, bookId);

            // Step 2: Delete the book itself
            deleteBookFromBooks(conn, bookId);

            // Commit the transaction
            conn.commit();
            System.out.println("Book and associated orders deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete book and associated orders. Rolling back transaction.");
        }
    }

    private void deleteOrderDetails(Connection conn, int bookId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM orderdetail WHERE book_id = ?")) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }

    private void deleteBookFromBooks(Connection conn, int bookId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM books WHERE book_id = ?")) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }

}



