package com.example.whynotpc.Records;

import com.example.whynotpc.Models.OrderItem;

import java.util.List;

public record OrderDTO(Integer id, List<OrderItem> itemlist, Float total, Integer userId) { }
