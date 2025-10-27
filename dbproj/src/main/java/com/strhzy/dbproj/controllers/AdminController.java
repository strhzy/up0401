package com.strhzy.dbproj.controllers;

import com.strhzy.dbproj.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService service;
    public AdminController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", service.findAll());
        return "adminusers";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        service.findById(id).ifPresent(u -> model.addAttribute("user", u));
        return "adminedituser";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("user") com.strhzy.dbproj.models.User user, BindingResult br) {
        if (br.hasErrors()) {
            return "adminedituser";
        }
        user.setId(id);
        service.save(user);
        return "redirect:/admin";
    }
}
