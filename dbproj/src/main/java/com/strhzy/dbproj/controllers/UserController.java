package com.strhzy.dbproj.controllers;

import com.strhzy.dbproj.models.User;
import com.strhzy.dbproj.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            service.registerUser(username, password);
            model.addAttribute("success", "Регистрация прошла успешно!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String username, @RequestParam String password, HttpServletResponse response, Model model) {
        try {
            User user = service.authenticate(username, password);
            Cookie cookie = new Cookie("username", user.getUsername());
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setHttpOnly(false);
            response.addCookie(cookie);

            // Добавляем cookie с ролью для фронтенда
            String role = (user.getRole() == null || user.getRole().isBlank()) ? "USER" : user.getRole();
            Cookie roleCookie = new Cookie("role", role);
            roleCookie.setPath("/");
            roleCookie.setMaxAge(7 * 24 * 60 * 60);
            roleCookie.setHttpOnly(false);
            response.addCookie(roleCookie);

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
        return "redirect:/";
    }
}