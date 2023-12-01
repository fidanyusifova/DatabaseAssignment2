package objects;

import java.awt.print.Book;
import java.util.List;

public class Author {
    private int authorId;

    private String authorName;

    private String country;

    private List<Book> books;

    public Author(String authorName, String country) {
        this.authorName = authorName;
        this.country = country;
    }

    public Author(int authorId, String authorName, String country) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.country = country;
    }

    @Override
    public String toString() {
        return "AuthorID: " + authorId +
                ", AuthorName: " + authorName +
                ", Country: " + country;
    }

    // Getters and Setters
    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
