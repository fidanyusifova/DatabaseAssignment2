import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookCrud {

    public boolean insertBook(Connection conn, Book newBook) {
        if (newBook.getTitle() == null || newBook.getGenre() == null || newBook.getAuthorId() <= 0 || newBook.getPublicationYear() <= 0 || newBook.getStockQuantity() < 0) {
            System.out.println("Invalid book details provided.");
            return false;
        }

        String query = "INSERT INTO Books (title, genre, stock_quantity, author_id, publication_year) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, newBook.getTitle());
            pstmt.setString(2, newBook.getGenre());
            pstmt.setInt(4, newBook.getAuthorId());
            pstmt.setInt(5, newBook.getPublicationYear());
            pstmt.setInt(3, newBook.getStockQuantity());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    newBook.setBookId(generatedId);
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Insertion failed! " + e.getMessage());
        }
        return false;
    }

    public List<Book> getAllBooks(Connection conn) {
        String sql = "SELECT * FROM Books ORDER BY book_id";
        List<Book> books = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Book book = extractBookFromResultSet(rs);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            System.out.println("Retrieve failed! " + e.getMessage());
        }
        return books;
    }

    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        int bookId = rs.getInt("book_id");
        String title = rs.getString("title");
        String genre = rs.getString("genre");
        int stockQuantity = rs.getInt("stock_quantity");
        int authorId = rs.getInt("author_id");
        int publicationYear = rs.getInt("publication_year");

        return new Book(bookId, title, genre, stockQuantity, authorId, publicationYear);
    }

    public boolean updateBook(Connection conn, int bookIdToUpdate, int newStockQuantity) {
        if (conn == null) {
            System.out.println("Invalid database connection.");
            return false;
        }

        String sql = "UPDATE Books SET stock_quantity = ? WHERE book_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newStockQuantity);
            pstmt.setInt(2, bookIdToUpdate);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Update failed! " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBook(Connection conn, int bookIdToDelete) {
        if (conn == null) {
            System.out.println("Invalid database connection.");
            return false;
        }

        String sql = "DELETE FROM Books WHERE book_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookIdToDelete);

            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Delete failed! " + e.getMessage());
            return false;
        }
    }


}



