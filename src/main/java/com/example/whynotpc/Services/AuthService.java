package com.example.whynotpc.Services;

import com.example.whynotpc.Models.Order;
import com.example.whynotpc.Models.Role;
import com.example.whynotpc.Models.User;
import com.example.whynotpc.Records.AuthResponse;
import com.example.whynotpc.Records.LoginRequest;
import com.example.whynotpc.Records.RegisterRequest;
import com.example.whynotpc.Repos.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepo orderRepo;

    public AuthResponse register(RegisterRequest request) {
        Order order = new Order();
        var user = userService.create(User.of(
                        request.username(),
                        request.email(),
                        passwordEncoder.encode(request.password()),
                        Role.USER,
                        LocalDateTime.now(),
                        order
                )
        );
        order.setUser(user);
        orderRepo.save(order);
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse("Authentication success", jwtToken, user.getUsername(),
                user.getEmail(), user.getCreatedAt().toString());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException exception) {
            return new AuthResponse("Authentication fail: Invalid credentials",
                    null, null, null, null);
        }
        var user = userService.findByUsername(request.username())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse("Authentication success", jwtToken, user.getUsername(),
                user.getEmail(), user.getCreatedAt().toString());
    }

    public AuthResponse getInfo(String username) {
        var user = userService.findByUsername(username).get();
        return new AuthResponse("User info", null, user.getUsername(),
                user.getEmail(), user.getCreatedAt().toString());
    }
}
