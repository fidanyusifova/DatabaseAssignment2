public class Book {
    private int bookId;
    private String title;
    private String genre;
    private int stockQuantity;
    private int authorId;
    private int publicationYear;

    // Constructors
    public Book() {
    }

    public Book(int bookId, String title, String genre, int stockQuantity, int authorId, int publicationYear) {
        this.bookId = bookId;
        this.title = title;
        this.genre = genre;
        this.stockQuantity = stockQuantity;
        this.authorId = authorId;
        this.publicationYear = publicationYear;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
