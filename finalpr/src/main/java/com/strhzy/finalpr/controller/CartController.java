package com.strhzy.finalpr.controller;

import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.model.OrderItem;
import com.strhzy.finalpr.model.Order;
import com.strhzy.finalpr.service.HttpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CartController {

    private void applySessionToken(HttpSession session) {
        Object token = session.getAttribute("jwt");
        if (token instanceof String) {
            HttpService.setToken((String) token);
        } else {
            HttpService.clearToken();
        }
    }

    @GetMapping("/cart")
    public String cartPage(Model model, HttpSession session) {
        applySessionToken(session);
        boolean isAuth = HttpService.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuth);
        Object user = session.getAttribute("user");
        if (user instanceof Customer) model.addAttribute("customer", (Customer) user);
        model.addAttribute("title", "Корзина");

        List<OrderItem> items = new ArrayList<>();
        Object cartObj = session.getAttribute("cart");
        if (cartObj instanceof List) {
            try {
                items = (List<OrderItem>) cartObj;
            } catch (Exception ignored) {
                items = new ArrayList<>();
            }
        }

        model.addAttribute("items", items);
        double total = items.stream().mapToDouble(i -> (i.getPrice() == null ? 0.0 : i.getPrice()) * (i.getQuantity() == null ? 0 : i.getQuantity())).sum();
        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        applySessionToken(session);
        Object cartObj = session.getAttribute("cart");
        if (cartObj instanceof List) {
            List<OrderItem> cart = (List<OrderItem>) cartObj;
            List<OrderItem> updated = cart.stream().filter(it -> it.getProductId() == null || !it.getProductId().equals(productId)).collect(Collectors.toList());
            session.setAttribute("cart", updated);
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        applySessionToken(session);
        if (!HttpService.isAuthenticated()) {
            return "redirect:/login";
        }

        Object cartObj = session.getAttribute("cart");
        List<OrderItem> cart = new ArrayList<>();
        if (cartObj instanceof List) {
            cart = (List<OrderItem>) cartObj;
        }

        if (cart.isEmpty()) {
            return "redirect:/cart?empty";
        }

        try {
            Object user = session.getAttribute("user");
            Long customerId = null;
            if (user instanceof Customer c) customerId = c.getId();
            if (customerId == null) {
                return "redirect:/login";
            }

            double total = cart.stream().mapToDouble(i -> (i.getPrice() == null ? 0.0 : i.getPrice()) * (i.getQuantity() == null ? 0 : i.getQuantity())).sum();

            Order orderPayload = new com.strhzy.finalpr.model.Order();
            orderPayload.setCustomerId(customerId);
            orderPayload.setOrderDate(java.time.LocalDateTime.now());
            orderPayload.setStatus("NEW");
            orderPayload.setTotalAmount(total);

            Order savedOrder = HttpService.post("/orders", orderPayload, com.strhzy.finalpr.model.Order.class);

            if (savedOrder == null || savedOrder.getId() == null) {
                throw new RuntimeException("Failed to create order or returned id is null");
            }

            Long orderId = savedOrder.getId();

            for (OrderItem it : cart) {
                com.strhzy.finalpr.model.OrderItem oi = new com.strhzy.finalpr.model.OrderItem();
                oi.setOrderId(orderId);
                oi.setProductId(it.getProductId());
                oi.setQuantity(it.getQuantity());
                oi.setPrice(it.getPrice());

                try {
                    HttpService.post("/order-items", oi, com.strhzy.finalpr.model.OrderItem.class);
                } catch (Exception ex) {
                    try {
                        HttpService.delete("/orders/" + orderId, Object.class);
                    } catch (Exception delEx) {
                        System.err.println("Failed to delete order after order-item creation failure: " + delEx.getMessage());
                    }
                    throw ex;
                }
            }

            session.removeAttribute("cart");
            return "redirect:/profile?tab=orders";
        } catch (Exception e) {
            System.err.println("Checkout failed (local cart): " + e.getMessage());
            return "redirect:/cart?error";
        }
    }
}
