package com.example.whynotpc.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> itemList = new ArrayList<>();

    @ToString.Exclude
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;

    public Float getTotal() {
        return (float) itemList.stream().mapToDouble(OrderItem::getTotal).sum() * 100.0f / 100.0f;
    }
}
