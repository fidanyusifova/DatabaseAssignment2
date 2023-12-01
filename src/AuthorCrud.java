//package methods;

//import models.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorCrud {
    private static final String dbname = "BookStore";
    private static final String user = "postgres";
    private static final String pass = "fidan123";

    public Connection connect_to_db() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore", "postgres", "fidan123");
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

    public void insertAuthor(Author author) {
        try (Connection conn = connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO authors (author_name, country) VALUES (?, ?, ?)")) {
            pstmt.setString(1, author.getAuthorName());
            pstmt.setString(3, author.getCountry());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Author inserted successfully");
            } else {
                System.out.println("Failed to insert author");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Author> retrieveAllAuthors() {
        List<Author> authors = new ArrayList<>();

        try (Connection conn = connect_to_db();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM authors")) {

            while (rs.next()) {
                int authorId = rs.getInt("author_id");
                String authorName = rs.getString("author_name");
                String country = rs.getString("country");

                // Create an Author object
                Author author = new Author(authorId, authorName, country);

                // Add the author to the list
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    public Author retrieveAuthorById(int authorId) {
        try (Connection conn = connect_to_db();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM authors WHERE author_id = ?")) {

            pstmt.setInt(1, authorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String authorName = rs.getString("author_name");

                    String country = rs.getString("country");

                    return new Author(authorId, authorName, country);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if the author is not found or an error occurs
    }

    public void updateAuthor(int authorId, String newAuthorName, String newCountry) throws SQLException {
        // Retrieve existing author
        Author existingAuthor = findAuthorById(authorId);

        if (existingAuthor == null) {
            System.out.println("Author with ID " + authorId + " not found.");
            return;
        }

        // Update attributes based on provided values
        if (!" ".equals(newAuthorName)) {
            existingAuthor.setAuthorName(newAuthorName);
        }
        if (!" ".equals(newCountry)) {
            existingAuthor.setCountry(newCountry);
        }

        // Update the author in the database
        try (Connection conn = connect_to_db()) {
            String updateQuery = "UPDATE authors SET author_name = ?, country = ? WHERE author_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setString(1, existingAuthor.getAuthorName());
                pstmt.setString(3, existingAuthor.getCountry());
                pstmt.setInt(4, authorId);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Author updated successfully");
                } else {
                    System.out.println("Failed to update author");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void deleteAuthor(int authorId) {
        try (Connection conn = connect_to_db()) {
            conn.setAutoCommit(false);

            // Step 1: Delete books associated with the author
            deleteBooksByAuthor(conn, authorId);

            // Step 2: Delete the author itself
            deleteAuthorFromAuthors(conn, authorId);

            // Commit the transaction
            conn.commit();
            System.out.println("Author and associated books deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete author and associated books. Rolling back transaction.");
        }
    }

    private void deleteBooksByAuthor(Connection conn, int authorId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM books WHERE author_id = ?")) {
            pstmt.setInt(1, authorId);
            pstmt.executeUpdate();
        }
    }

    private void deleteAuthorFromAuthors(Connection conn, int authorId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "DELETE FROM authors WHERE author_id = ?")) {
            pstmt.setInt(1, authorId);
            pstmt.executeUpdate();
        }
    }

}

