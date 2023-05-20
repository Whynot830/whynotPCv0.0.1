package com.example.whynotpc.Repos;

import com.example.whynotpc.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    Order findByUserId(Integer userId);
    Order findByUserUsername(String username);
}
