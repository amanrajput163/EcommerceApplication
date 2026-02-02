package com.example.controller;

import com.example.entity.User;
import com.example.response.ApiResponse;
import com.example.security.JwtUtil;
import com.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody User user) {
        return new ApiResponse<>(true, "User registered",
                userService.registerUser(user));
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody Map<String, String> req) {

        User user = userService.findByUsername(req.get("username"))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.get("password"), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new ApiResponse<>(true, "Login successful",
                Map.of("token", token, "role", user.getRole()));
    }

    // ✅ GET USERS WITH PAGINATION
    @GetMapping("/page")
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }
}
