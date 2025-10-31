package com.strhzy.finalpr.controller;

import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.service.HttpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class AuthController {

    public static class AuthRequest {
        public String username;
        public String password;
        public AuthRequest() {}
        public AuthRequest(String username, String password) { this.username = username; this.password = password; }
    }

    public static class AuthResponse {
        public String token;
        public Customer user;
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        if (session.getAttribute("jwt") != null) {
            return "redirect:/";
        }
        model.addAttribute("title", "Вход");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        try {
            AuthRequest req = new AuthRequest(username, password);
            AuthResponse resp = HttpService.post("/auth/login", req, AuthResponse.class);

            if (resp != null && resp.token != null && !resp.token.isEmpty()) {
                session.setAttribute("jwt", resp.token);
                HttpService.setToken(resp.token);

                if (resp.user != null) {
                    session.setAttribute("user", resp.user);
                    session.setAttribute("username", resp.user.getName() != null ? resp.user.getName() : resp.user.getUsername());
                } else {
                    Customer tmp = new Customer();
                    tmp.setUsername(username);
                    tmp.setName(username);
                    session.setAttribute("user", tmp);
                    session.setAttribute("username", username);
                }

                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/login?error";
    }

    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
        if (session.getAttribute("jwt") != null) {
            return "redirect:/profile";
        }
        model.addAttribute("title", "Регистрация");
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, HttpSession session) {
        try {
            Map<String, String> payload = Map.of("username", username, "password", password, "name", username);
            AuthResponse resp = HttpService.post("/auth/register", payload, AuthResponse.class);

            if (resp != null && resp.token != null && !resp.token.isEmpty()) {
                session.setAttribute("jwt", resp.token);
                HttpService.setToken(resp.token);

                if (resp.user != null) {
                    session.setAttribute("user", resp.user);
                    session.setAttribute("username", resp.user.getName() != null ? resp.user.getName() : resp.user.getUsername());
                } else {
                    Customer tmp = new Customer();
                    tmp.setUsername(username);
                    tmp.setName(username);
                    session.setAttribute("user", tmp);
                    session.setAttribute("username", username);
                }

                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/register?error";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        HttpService.clearToken();
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logoutGet(HttpSession session) {
        return logout(session);
    }
}
