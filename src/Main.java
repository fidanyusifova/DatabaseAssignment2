 import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

            public class Main {

                public static void main(String[] args) {
                    Scanner scanner = new Scanner(System.in);

                    // Establish a database connection
                    Connection conn = null;
                    try {
                        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore", "postgres", "fidan123");
                        System.out.println("Connected to the PostgreSQL database.");

                        // Create an instance of BookCrud
                        BookCrud bookCrud = new BookCrud();

                        // Insert a new book
                        Book newBook = new Book();
                        //System.out.println("Enter the book ID of the new book:");
                        // newBook.setAuthorId(scanner.nextInt());
                        System.out.println("Enter the title of the new book:");
                        newBook.setTitle(scanner.nextLine());
                        System.out.println("Enter the genre of the new book:");
                        newBook.setGenre(scanner.nextLine());
                        System.out.println("Enter the stock quantity of the new book:");
                        newBook.setStockQuantity(scanner.nextInt());
                        System.out.println("Enter the author ID of the new book:");
                        newBook.setAuthorId(scanner.nextInt());
                        System.out.println("Enter the publication year of the new book:");
                        newBook.setPublicationYear(scanner.nextInt());

                        boolean isInserted = bookCrud.insertBook(conn, newBook);
                        System.out.println("Insert success: " + isInserted);

                        // Update a book's stock quantity
                        System.out.println("Enter the ID of the book to update:");
                        int bookIdToUpdate = scanner.nextInt();
            /*if (scanner.hasNextInt()) {
                bookIdToUpdate = scanner.nextInt();
                // Use the bookIdToUpdate here for updating

            } else {
                String invalidInput = scanner.next(); // Read and discard invalid input
                System.out.println("Invalid input: Please enter a valid ID (numeric value).");
            }*/
                        System.out.println("Enter the new stock quantity:");
                        int newStockQuantity = scanner.nextInt();
                        boolean isUpdated = bookCrud.updateBook(conn, bookIdToUpdate, newStockQuantity);
                        System.out.println("Update success: " + isUpdated);

                        // Delete a book
                        System.out.println("Enter the ID of the book to delete:");
                        int bookIdToDelete = scanner.nextInt();
                        boolean isDeleted = bookCrud.deleteBook(conn, bookIdToDelete);
                        System.out.println("Delete success: " + isDeleted);

                        // Retrieve all books
                        System.out.println("All Books:");
                        bookCrud.getAllBooks(conn).forEach(book -> {
                            System.out.println("Book ID: " + book.getBookId() + ", Title: " + book.getTitle());
                        });

                    } catch (SQLException e) {
                        System.err.println("SQLException: " + e.getMessage());
                        System.err.println("SQLState: " + e.getSQLState());
                        System.err.println("VendorError: " + e.getErrorCode());
                    } finally {
                        // Close the connection in the finally block
                        if (conn != null) {
                            try {
                                conn.close();
                                System.out.println("Connection closed.");
                            } catch (SQLException e) {
                                System.err.println("Error closing connection: " + e.getMessage());
                            }
                        }
                    }


                    // Close the scanner
                    scanner.close();
                }
            }