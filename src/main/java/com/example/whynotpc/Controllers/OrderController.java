package com.example.whynotpc.Controllers;

import com.example.whynotpc.Models.Order;
import com.example.whynotpc.Records.OrderDTO;
import com.example.whynotpc.Services.OrderService;
import com.example.whynotpc.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<OrderDTO>> readAll() {
        List<Order> orders = orderService.readAll();

        List<OrderDTO> orderRecords = orders
                .stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getItemList(),
                        order.getTotal(),
                        order.getUser().getId()
                )).collect(Collectors.toList());
        return ResponseEntity.ok(orderRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> read(@PathVariable Integer id) {
        Optional<Order> optionalOrder = orderService.readOne(id);

        if (optionalOrder.isEmpty())
            return ResponseEntity.notFound().build();
        var order = optionalOrder.get();
        return ResponseEntity.ok(new OrderDTO(
                order.getId(),
                order.getItemList(),
                order.getTotal(),
                order.getUser().getId())
        );
    }

    @GetMapping("/user")
    public ResponseEntity<OrderDTO> readByUser(Authentication authentication) {
        var userId = userService.findByUsername(authentication.getName()).get().getId();
        var order = orderService.readByUserId(userId);
        return ResponseEntity.ok(new OrderDTO(
                order.getId(),
                order.getItemList(),
                order.getTotal(),
                order.getUser().getId())
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<OrderDTO> readByUserId(@PathVariable Integer id) {
        var order = orderService.readByUserId(id);
        return ResponseEntity.ok(new OrderDTO(
                order.getId(),
                order.getItemList(),
                order.getTotal(),
                order.getUser().getId())
        );
    }

    @PostMapping("/add-item/{productId}")
    public ResponseEntity<Void> addItem(@PathVariable Integer productId, Authentication authentication) {
        var userId = userService.findByUsername(authentication.getName()).get().getId();
        orderService.addItem(productId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-item/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Integer productId, Authentication authentication) {
        var userId = userService.findByUsername(authentication.getName()).get().getId();
        boolean flag = orderService.removeItem(productId, userId);
        return flag ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clear(Authentication authentication) {
        orderService.clear(authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(Authentication authentication) {
        var username = authentication.getName();
        orderService.confirm(username);
        return ResponseEntity.ok().build();
    }
}
