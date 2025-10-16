package com.burgerstream.backend.model.order;

import com.burgerstream.backend.model.menu.MenuItem;
import com.burgerstream.backend.model.menu.SizeOption;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(name = "size_option_id")
    private SizeOption sizeOption;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "item_end_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal itemEndPrice = BigDecimal.ZERO;

    public OrderItem(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public SizeOption getSizeOption() {
        return sizeOption;
    }

    public void setSizeOption(SizeOption sizeOption) {
        this.sizeOption = sizeOption;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
        BigDecimal newPrice = itemEndPrice.multiply(BigDecimal.valueOf(amount));
        this.setItemEndPrice(newPrice);
    }

    public BigDecimal getItemEndPrice() {
        return itemEndPrice;
    }

    public void setItemEndPrice(BigDecimal itemEndPrice) {
        this.itemEndPrice = itemEndPrice;
    }
}