package com.strhzy.finalpr.controller;

import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.service.HttpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private boolean hasAdminRole(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user instanceof Customer customer) {
            return "ADMIN".equalsIgnoreCase(customer.getRole());
        }
        return false;
    }

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        if (!hasAdminRole(session)) {
            return "redirect:/";
        }
        try {
            Customer[] users = HttpService.get("/customers", Customer[].class);
            model.addAttribute("users", users == null ? List.of() : List.of(users));
        } catch (Exception e) {
            model.addAttribute("users", List.of());
        }
        model.addAttribute("title", "Пользователи");
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!hasAdminRole(session)) {
            return "redirect:/";
        }
        try {
            Customer user = HttpService.get("/customers/" + id, Customer.class);
            model.addAttribute("user", user);
        } catch (Exception e) {
            model.addAttribute("user", new Customer());
        }
        model.addAttribute("title", "Редактировать пользователя");
        return "admin/edit-user";
    }

    @PostMapping("/users/edit")
    public String saveUser(@RequestParam Long id,
                           @RequestParam String name,
                           @RequestParam String username,
                           @RequestParam String role,
                           @RequestParam(required = false) String phone,
                           @RequestParam(required = false) String address,
                           HttpSession session) {
        if (!hasAdminRole(session)) {
            return "redirect:/";
        }
        try {
            Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("name", name);
            payload.put("username", username);
            payload.put("role", (role != null && !role.isBlank()) ? role : "USER");
            payload.put("password", HttpService.get("/customers/" + id, Customer.class).getPassword());
            if (phone != null && !phone.isBlank()) payload.put("phone", phone);
            if (address != null && !address.isBlank()) payload.put("address", address);

            HttpService.put("/customers/" + id, payload, String.class);
            return "redirect:/admin/users?success";
        } catch (Exception e) {
            return "redirect:/admin/users?error";
        }
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (!hasAdminRole(session)) {
            return "redirect:/";
        }
        else {
            try {
                HttpService.delete("/customers/" + id, String.class);
                return "redirect:/admin/users";
            } catch (Exception e) {
                return "redirect:/admin/users";
            }
        }
    }
}
