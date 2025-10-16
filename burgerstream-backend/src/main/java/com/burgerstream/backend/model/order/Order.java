package com.burgerstream.backend.model.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false, updatable = false)
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "order_end_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal orderEndPrice = BigDecimal.ZERO;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getOrderEndPrice() {
        return orderEndPrice;
    }

    public void setOrderEndPrice(BigDecimal orderEndPrice) {
        this.orderEndPrice = orderEndPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem){
        if (orderItem.getId() == null || orderItems.stream().noneMatch(item -> item.getId().equals(orderItem.getId()))){
            orderItems.add(orderItem);
            orderEndPrice = orderEndPrice.add(orderItem.getItemEndPrice());
            orderItem.setOrder(this);
        }
    }

    public void removeOrderItem(OrderItem orderItem){
        if (orderItems.stream().anyMatch(item -> item.getId().equals(orderItem.getId()))) {
            orderEndPrice = orderEndPrice.subtract(orderItem.getItemEndPrice());
            orderItems.remove(orderItem);
            orderItem.setOrder(null);
        }
    }
}