package objects;

import java.time.LocalDate;
//import java.util.Date;
import java.util.List;

public class Order {

    private int orderId;

    private LocalDate orderDate;

    private double totalAmount;

    private Customer customer;

    private List<OrderDetail> orderDetails;

    public Order(int orderId, LocalDate orderDate, double totalAmount,Customer customer, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.orderDetails = orderDetails;
    }

    public Order(int orderId, LocalDate orderDate, double totalAmount) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "OrderId: " + orderId +
                ", OrderDate: " + orderDate +
                ", TotalAmount: " + totalAmount;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }



    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}

