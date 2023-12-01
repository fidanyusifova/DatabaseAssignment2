public class Author {
    private int authorId;
    private String authorName;

    // Constructors
    public Author() {
    }

    public Author(String authorName) {
        this.authorName = authorName;
    }

    // Getters and setters
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

    // toString() method for printing the Author details
    @Override
    public String toString() {
        return "Author ID: " + authorId + ", Author Name: " + authorName;
    }
}
