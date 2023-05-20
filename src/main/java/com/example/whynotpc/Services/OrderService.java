package com.example.whynotpc.Services;

import com.example.whynotpc.Models.Order;
import com.example.whynotpc.Models.OrderItem;
import com.example.whynotpc.Repos.OrderItemRepo;
import com.example.whynotpc.Repos.OrderRepo;
import com.example.whynotpc.Repos.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final OrderItemRepo orderItemRepo;
    private final EmailService emailService;

    public void addItem(Integer productId, Integer userId) {
        var order = orderRepo.findByUserId(userId);
        for (var item : order.getItemList())
            if (Objects.equals(productId, item.getProduct().getId())) {
                item.setQuantity(item.getQuantity() + 1);
                orderItemRepo.save(item);
                return;
            }
        var orderItem = OrderItem.of(productRepo.findById(productId).get());
        orderItem.setOrder(order);
        orderItemRepo.save(orderItem);
    }

    public boolean removeItem(Integer productId, Integer userId) {
        var order = orderRepo.findByUserId(userId);
        var isPresent = false;
        OrderItem presentItem = null;
        for (var item : order.getItemList())
            if (Objects.equals(productId, item.getProduct().getId())) {
                isPresent = true;
                presentItem = item;
                break;
            }
        if (!isPresent)
            return false;
        if (presentItem.getQuantity() == 1) {
            orderItemRepo.deleteById(presentItem.getId());
            return true;
        }
        presentItem.setQuantity(presentItem.getQuantity() - 1);
        orderItemRepo.save(presentItem);
        return true;
    }

    public List<Order> readAll() {
        return orderRepo.findAll();
    }

    public Optional<Order> readOne(Integer id) {
        return orderRepo.findById(id);
    }

    public Order readByUserId(Integer userId) {
        return orderRepo.findByUserId(userId);
    }

    public void clear(String username) {
        var order = orderRepo.findByUserUsername(username);
        for (var item : order.getItemList()) {
            orderItemRepo.deleteById(item.getId());
        }
    }

    public void confirm(String username) {
        var order = orderRepo.findByUserUsername(username);
        var userEmail = order.getUser().getEmail();
        String subject = "Order from whynotPC";
        if (order.getItemList().isEmpty()) {
            String message = """
                    It seems, that you tried to confirm your order, but you haven't added anything to cart yet.

                    Best regards,
                    whynotPC
                    """;

            emailService.sendMail(userEmail, subject, message);
            return;
        }

        String message = """
                Congratulations, you've just confirmed your order # %d:
                %s
                        
                Total: USD $%.2f
                                
                Best regards,
                whynotPC
                """.formatted(
                order.getId(),
                order.getItemList()
                        .stream()
                        .map(OrderItem::toString)
                        .collect(Collectors.joining("\n")),
                order.getTotal());

        clear(username);
        emailService.sendMail(userEmail, subject, message);

    }
}
