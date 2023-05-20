package com.example.whynotpc.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer quantity;
    @ManyToOne
    private Product product;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Order order;

    public Float getTotal() {
        return quantity * product.getPrice();
    }

    public static OrderItem of(Product product) {
        OrderItem item = new OrderItem();
        item.setQuantity(1);
        item.setOrder(null);
        item.setProduct(product);
        return item;
    }

    @Override
    public String toString() {
        return "%dx %s | USD $%.2f".formatted(quantity, getProduct().getTitle(), getProduct().getPrice());
    }
}
