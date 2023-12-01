import java.sql.*;
import java.util.Scanner;



public class AuthorCrud {

    public static void main(String[] args) throws SQLException {
        // Database connection parameters
       /* String url = "jdbc:postgresql://localhost:5432/YourDatabaseName";
        String user = "your_username";
        String password = "your_password";*/

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore", "postgres", "fidan123");
            System.out.println("Connected to the PostgreSQL database.");

            // Perform CRUD operations
            AuthorCrud authorCrud = new AuthorCrud();
            authorCrud.insertAuthor(conn);
            authorCrud.getAuthors(conn);
            authorCrud.updateAuthor(conn);
            authorCrud.deleteAuthor(conn);

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());
        }
    }

            // Insert operation
            public void insertAuthor (Connection conn){
                try {
                    String insertQuery = "INSERT INTO Authors (author_name) VALUES (?)";

                    // Taking user input for author's name
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter author name:");
                    String authorName = scanner.nextLine();

                    try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                        pstmt.setString(1, authorName);
                        int rowsInserted = pstmt.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Author inserted successfully!");
                        } else {
                            System.out.println("Author insertion failed!");
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error inserting author: " + e.getMessage());
                }
            }

            // Retrieve operation
            public void getAuthors (Connection conn){
                try {
                    String selectQuery = "SELECT * FROM Authors";
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(selectQuery)) {
                        while (rs.next()) {
                            int authorId = rs.getInt("author_id");
                            String authorName = rs.getString("author_name");
                            System.out.println("Author ID: " + authorId + ", Author Name: " + authorName);
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error retrieving authors: " + e.getMessage());
                }
            }

            // Update operation
            public void updateAuthor(Connection conn) {
                Scanner scanner = new Scanner(System.in);

                try {
                    String updateQuery = "UPDATE Authors SET author_name = ? WHERE author_id = ?";
                    String selectQuery = "SELECT * FROM Authors WHERE author_id = ?";

                    System.out.println("Enter author ID to update:");
                    int authorIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    System.out.println("Enter updated author name:");
                    String updatedAuthorName = scanner.nextLine();

                    // Check if the author ID exists before updating
                    try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                        selectStmt.setInt(1, authorIdToUpdate);
                        ResultSet resultSet = selectStmt.executeQuery();
                        if (!resultSet.next()) {
                            System.out.println("Author ID does not exist. Update failed!");
                            return;
                        }
                    }

                    try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                        pstmt.setString(1, updatedAuthorName);
                        pstmt.setInt(2, authorIdToUpdate);
                        int rowsUpdated = pstmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Author updated successfully!");
                        } else {
                            System.out.println("Author update failed!");
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("SQL Exception: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    scanner.close();
                }
            }

    public void deleteAuthor(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        try {
            String deleteQuery = "DELETE FROM Authors WHERE author_id = ?";
            String selectQuery = "SELECT * FROM Authors WHERE author_id = ?";

            System.out.println("Enter author ID to delete:");
            int authorIdToDelete = scanner.nextInt();

            // Check if the author ID exists before deletion
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, authorIdToDelete);
                ResultSet resultSet = selectStmt.executeQuery();
                if (!resultSet.next()) {
                    System.out.println("Author ID does not exist. Deletion failed!");
                    return;
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setInt(1, authorIdToDelete);
                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Author deleted successfully!");
                } else {
                    System.out.println("Author deletion failed!");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }


}


