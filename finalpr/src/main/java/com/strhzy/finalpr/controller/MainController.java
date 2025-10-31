package com.strhzy.finalpr.controller;

import com.strhzy.finalpr.model.Category;
import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.model.Manufacturer;
import com.strhzy.finalpr.model.Product;
import com.strhzy.finalpr.service.HttpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    private void applySessionToken(HttpSession session) {
        Object token = session.getAttribute("jwt");
        if (token instanceof String) {
            HttpService.setToken((String) token);
        } else {
            HttpService.clearToken();
        }
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        applySessionToken(session);
        boolean isAuth = HttpService.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuth);
        Object user = session.getAttribute("user");
        if (user instanceof Customer customer) model.addAttribute("customer", customer);
        model.addAttribute("title", "Главная");

        List<Product> products = new ArrayList<>();
        try {
            Product[] arr = HttpService.get("/products?limit=6", Product[].class);
            if (arr != null) products = Arrays.asList(arr);
        } catch (Exception e) {
            System.err.println("Failed to load featured products: " + e.getMessage());
        }
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/products")
    public String products(Model model, HttpSession session,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) Long manufacturerId,
                           @RequestParam(required = false) String q,
                           @RequestParam(required = false, defaultValue = "") String sort) {
        applySessionToken(session);
        boolean isAuth = HttpService.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuth);
        Object user = session.getAttribute("user");
        if (user instanceof Customer customer) model.addAttribute("customer", customer);
        model.addAttribute("title", "Каталог");

        List<Product> products = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Manufacturer> manufacturers = new ArrayList<>();

        try {
            List<String> params = new ArrayList<>();
            if (categoryId != null) params.add("categoryId=" + categoryId);
            if (manufacturerId != null) params.add("manufacturerId=" + manufacturerId);
            if (q != null && !q.isBlank()) params.add("q=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
            if (sort != null && !sort.isBlank()) params.add("sort=" + URLEncoder.encode(sort, StandardCharsets.UTF_8));

            String path = "/products";
            if (!params.isEmpty()) path += "?" + String.join("&", params);

            Product[] arr = HttpService.get(path, Product[].class);
            if (arr != null) products = Arrays.asList(arr);
        } catch (Exception e) {
            System.err.println("Failed to load products: " + e.getMessage());
        }

        try {
            Category[] cArr = HttpService.get("/categories", Category[].class);
            if (cArr != null) categories = Arrays.asList(cArr);
        } catch (Exception e) {
            System.err.println("Failed to load categories: " + e.getMessage());
        }

        try {
            Manufacturer[] mArr = HttpService.get("/manufacturers", Manufacturer[].class);
            if (mArr != null) manufacturers = Arrays.asList(mArr);
        } catch (Exception e) {
            System.err.println("Failed to load manufacturers: " + e.getMessage());
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedManufacturerId", manufacturerId);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("sort", sort == null ? "" : sort);

        return "products";
    }
}
