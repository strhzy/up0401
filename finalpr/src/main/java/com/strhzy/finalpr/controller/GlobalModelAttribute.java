package com.strhzy.finalpr.controller;

import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.service.HttpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttribute {

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        Object jwtObj = session.getAttribute("jwt");
        boolean isAuth = jwtObj instanceof String && !((String) jwtObj).isEmpty();
        model.addAttribute("isAuthenticated", isAuth);

        if (isAuth) {
            HttpService.setToken((String) jwtObj);
        } else {
            HttpService.clearToken();
        }

        String username = null;
        String role = null;
        Object user = session.getAttribute("user");
        if (user instanceof Customer c) {
            if (c.getName() != null && !c.getName().isBlank()) {
                username = c.getName();
            } else if (c.getUsername() != null && !c.getUsername().isBlank()) {
                username = c.getUsername();
            } else {
                username = "Пользователь";
            }
            role = c.getRole();
        } else {
            Object un = session.getAttribute("username");
            if (un instanceof String) username = (String) un;
        }

        model.addAttribute("username", username != null ? username : "");
        model.addAttribute("role", role);
    }
}
