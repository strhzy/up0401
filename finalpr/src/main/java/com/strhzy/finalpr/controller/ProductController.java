package com.strhzy.finalpr.controller;

import com.strhzy.finalpr.model.Category;
import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.model.Manufacturer;
import com.strhzy.finalpr.model.OrderItem;
import com.strhzy.finalpr.model.Product;
import com.strhzy.finalpr.model.Review;
import com.strhzy.finalpr.service.HttpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class ProductController {

    private void applySessionToken(HttpSession session) {
        Object token = session.getAttribute("jwt");
        if (token instanceof String) {
            HttpService.setToken((String) token);
        } else {
            HttpService.clearToken();
        }
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model, HttpSession session) {
        applySessionToken(session);
        boolean isAuth = HttpService.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuth);
        Object user = session.getAttribute("user");
        if (user instanceof Customer customer) model.addAttribute("customer", customer);

        try {
            Product product = HttpService.get("/products/" + id, Product.class);
            if (product != null) {
                model.addAttribute("product", product);

                String categoryName = null;
                try {
                    if (product.getCategoryId() != null) {
                        Category cat = HttpService.get("/categories/" + product.getCategoryId(), Category.class);
                        if (cat != null && cat.getName() != null && !cat.getName().isBlank()) categoryName = cat.getName();
                    }
                } catch (Exception ex) {
                }
                if (categoryName == null) categoryName = product.getCategoryId() != null ? String.valueOf(product.getCategoryId()) : "Неизвестно";
                model.addAttribute("categoryName", categoryName);

                String manufacturerName = null;
                try {
                    if (product.getManufacturerId() != null) {
                        Manufacturer m = HttpService.get("/manufacturers/" + product.getManufacturerId(), Manufacturer.class);
                        if (m != null && m.getName() != null && !m.getName().isBlank()) manufacturerName = m.getName();
                    }
                } catch (Exception ex) {
                }
                if (manufacturerName == null) manufacturerName = product.getManufacturerId() != null ? String.valueOf(product.getManufacturerId()) : "Неизвестно";
                model.addAttribute("manufacturerName", manufacturerName);

                try {
                    Review[] arr = HttpService.get("/reviews?productId=" + id, Review[].class);
                    if (arr != null) model.addAttribute("reviews", Arrays.asList(arr));
                    else model.addAttribute("reviews", java.util.Collections.emptyList());

                    Map<Long, String> reviewerNames = new HashMap<>();
                    if (arr != null) {
                        for (Review r : arr) {
                            Long cid = r.getCustomerId();
                            if (cid == null) continue;
                            if (!reviewerNames.containsKey(cid)) {
                                try {
                                    Customer c = HttpService.get("/customers/" + cid, Customer.class);
                                    String display = null;
                                    if (c != null) {
                                        if (c.getName() != null && !c.getName().isBlank()) display = c.getName();
                                        else if (c.getUsername() != null && !c.getUsername().isBlank()) display = c.getUsername();
                                    }
                                    if (display == null) display = "Пользователь";
                                    reviewerNames.put(cid, display);
                                } catch (Exception ex) {
                                    reviewerNames.put(cid, "Пользователь");
                                }
                            }
                        }
                    }
                    model.addAttribute("reviewerNames", reviewerNames);

                } catch (Exception e) {
                    System.err.println("Failed to load reviews: " + e.getMessage());
                    model.addAttribute("reviews", java.util.Collections.emptyList());
                    model.addAttribute("reviewerNames", java.util.Collections.emptyMap());
                }

                model.addAttribute("title", product.getName());
                return "product-detail";
            }
        } catch (Exception e) {
            System.err.println("Failed to load product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            HttpSession session) {
        try {
            applySessionToken(session);
            Product product = HttpService.get("/products/" + productId, Product.class);

            double price = product != null && product.getPrice() != null ? product.getPrice() : 0.0;

            Object cartObj = session.getAttribute("cart");
            List<OrderItem> cart;
            if (cartObj instanceof List) {
                cart = (List<OrderItem>) cartObj;
            } else {
                cart = new ArrayList<>();
            }

            OrderItem existing = null;
            for (OrderItem it : cart) {
                if (it.getProductId() != null && it.getProductId().equals(productId)) {
                    existing = it; break;
                }
            }

            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + quantity);
            } else {
                OrderItem it = new OrderItem();
                it.setId(null);
                it.setOrderId(null);
                it.setProductId(productId);
                it.setQuantity(quantity);
                it.setPrice(price);
                cart.add(it);
            }

            session.setAttribute("cart", cart);
            return "redirect:/cart";
        } catch (Exception e) {
            System.err.println("Failed to add to local cart: " + e.getMessage());
            return "redirect:/products/" + productId + "?error";
        }
    }

    @PostMapping("/products/{id}/reviews")
    public String postReview(@PathVariable Long id,
                             @RequestParam Integer rating,
                             @RequestParam String comment,
                             HttpSession session) {
        applySessionToken(session);
        if (!HttpService.isAuthenticated()) {
            return "redirect:/login";
        }

        Object user = session.getAttribute("user");
        Long customerId = null;
        if (user instanceof Customer c) customerId = c.getId();
        if (customerId == null) {
            return "redirect:/products/" + id + "?error";
        }

        try {
            Review review = new Review();
            review.setProductId(id);
            review.setCustomerId(customerId);
            review.setRating(rating);
            review.setComment(comment);

            HttpService.post("/reviews", review, Object.class);
            return "redirect:/products/" + id + "?reviewed";
        } catch (Exception e) {
            System.err.println("Failed to post review: " + e.getMessage());
            return "redirect:/products/" + id + "?error";
        }
    }
}
