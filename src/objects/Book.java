package objects;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private int bookId;
    private String title;
    private String genre;
    private double price;
    private int stockQuantity;

    private Author author;
    private List<Order> orders = new ArrayList<>();

    public Book( String title, String genre, double price, int stockQuantity, Author author) {
        //this.bookId = bookId;
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.author = author;
    }


    @Override
    public String toString() {
        return "BookID: " + bookId +
                ", Title: " + title +
                ", Genre: " + genre +
                ", Price: " + price +
                ", StockQuantity: " + stockQuantity +
                ", Author: " + author +
                ", Orders: " + orders;
    }

    // Getters and Setters...

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
