package com.strhzy.finalpr.controller;

import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.model.Order;
import com.strhzy.finalpr.repository.CustomerRepository;
import com.strhzy.finalpr.service.HttpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private void applySessionToken(HttpSession session) {
        Object token = session.getAttribute("jwt");
        if (token instanceof String) {
            HttpService.setToken((String) token);
        } else {
            HttpService.clearToken();
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session, @RequestParam(required = false, defaultValue = "profile") String tab) {
        applySessionToken(session);
        boolean isAuth = HttpService.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuth);

        if (!isAuth) {
            return "redirect:/login";
        }

        model.addAttribute("activeTab", tab);

        Object user = session.getAttribute("user");
        if (user instanceof Customer customer) {
            model.addAttribute("customer", customer);
            String username = customer.getUsername() != null && !customer.getUsername().isEmpty()
                    ? customer.getUsername()
                    : (customer.getName() != null ? customer.getName() : "Пользователь");
            model.addAttribute("username", username);
        } else {
            String uname = null;
            Object unameAttr = session.getAttribute("username");
            if (unameAttr instanceof String) {
                uname = (String) unameAttr;
            } else if (user instanceof String) {
                uname = (String) user;
            }
            if (uname == null || uname.isBlank()) uname = "Пользователь";

            Customer tmp = new Customer();
            tmp.setUsername(uname);
            tmp.setName(uname);
            model.addAttribute("customer", tmp);
            model.addAttribute("username", uname);
        }

        if ("orders".equals(tab)) {
            List<Order> orders = new ArrayList<>();
            try {
                Object userObj = session.getAttribute("user");
                Long customerId = null;
                if (userObj instanceof Customer c) customerId = c.getId();
                if (customerId != null) {
                    Order[] arr = HttpService.get("/orders?customerId=" + customerId, Order[].class);
                    if (arr != null) orders = Arrays.asList(arr);
                }
            } catch (Exception e) {
                System.err.println("Failed to load orders for profile: " + e.getMessage());
            }
            model.addAttribute("orders", orders);
        }

        model.addAttribute("title", "Профиль");
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("customer") Customer updatedCustomer, HttpSession session) {
        applySessionToken(session);
        boolean isAuth = HttpService.isAuthenticated();
        if (!isAuth) {
            return "redirect:/login";
        }

        Object user = session.getAttribute("user");
        Long currentId = null;
        Customer current = null;
        if (user instanceof Customer cur) {
            current = cur;
            currentId = cur.getId();
        }

        try {
            if (currentId != null) {
                Customer toSave = new Customer();
                toSave.setId(current.getId());
                toSave.setName(current.getName());
                toSave.setUsername(current.getUsername());
                toSave.setPassword(current.getPassword());
                toSave.setAddress(current.getAddress());
                toSave.setPhone(current.getPhone());
                toSave.setRole(current.getRole());

                if (updatedCustomer.getName() != null) toSave.setName(updatedCustomer.getName());
                if (updatedCustomer.getUsername() != null) toSave.setUsername(updatedCustomer.getUsername());
                if (updatedCustomer.getPassword() != null && !updatedCustomer.getPassword().isBlank()) toSave.setPassword(updatedCustomer.getPassword());
                if (updatedCustomer.getAddress() != null) toSave.setAddress(updatedCustomer.getAddress());
                if (updatedCustomer.getPhone() != null) toSave.setPhone(updatedCustomer.getPhone());

                Customer saved = HttpService.put("/customers/" + currentId, toSave, Customer.class);

                session.setAttribute("user", saved);
                String display = saved.getName() != null && !saved.getName().isBlank()
                        ? saved.getName()
                        : (saved.getUsername() != null ? saved.getUsername() : "Пользователь");
                session.setAttribute("username", display);

                return "redirect:/profile?success";
            } else {
                Customer saved = customerRepository.save(updatedCustomer);
                session.setAttribute("user", saved);
                String display = saved.getName() != null && !saved.getName().isBlank()
                        ? saved.getName()
                        : (saved.getUsername() != null ? saved.getUsername() : "Пользователь");
                session.setAttribute("username", display);

                return "redirect:/profile?success";
            }
        } catch (Exception e) {
            return "redirect:/profile?error";
        }
    }
}
