package com.strhzy.finalpr.controller;

import com.strhzy.finalpr.model.Customer;
import com.strhzy.finalpr.model.Product;
import com.strhzy.finalpr.model.Order;
import com.strhzy.finalpr.service.HttpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ManagerController {

    private boolean hasNoManagerRole(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user instanceof Customer customer) {
            String role = customer.getRole();
            return !("MANAGER".equalsIgnoreCase(role));
        }
        return true;
    }

    @GetMapping("/manager")
    public String managerIndex(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) {
            return "redirect:/";
        }
        model.addAttribute("title", "Панель менеджера");
        return "manager/index";
    }

    @GetMapping("/manager/products")
    public String listProducts(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) {
            return "redirect:/";
        }
        try {
            Product[] products = HttpService.get("/products", Product[].class);
            model.addAttribute("products", products == null ? List.of() : List.of(products));
        } catch (Exception e) {
            model.addAttribute("products", List.of());
        }
        model.addAttribute("title", "Товары");
        return "manager/products";
    }

    @GetMapping("/manager/categories")
    public String listCategories(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            Object[] items = HttpService.get("/categories", Object[].class);
            model.addAttribute("items", items == null ? List.of() : List.of(items));
        } catch (Exception e) {
            model.addAttribute("items", List.of());
        }
        model.addAttribute("title", "Категории");
        model.addAttribute("entityName", "Категории");
        model.addAttribute("basePath", "/manager/categories");
        return "manager/categories";
    }

    @GetMapping("/manager/manufacturers")
    public String listManufacturers(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            Object[] items = HttpService.get("/manufacturers", Object[].class);
            model.addAttribute("items", items == null ? List.of() : List.of(items));
        } catch (Exception e) {
            model.addAttribute("items", List.of());
        }
        model.addAttribute("title", "Производители");
        model.addAttribute("entityName", "Производители");
        model.addAttribute("basePath", "/manager/manufacturers");
        return "manager/manufacturers";
    }

    @GetMapping("/manager/orders")
    public String listOrders(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            Object[] items = HttpService.get("/orders", Object[].class);
            model.addAttribute("items", items == null ? List.of() : List.of(items));
        } catch (Exception e) {
            model.addAttribute("items", List.of());
        }
        model.addAttribute("title", "Заказы");
        model.addAttribute("entityName", "Заказы");
        model.addAttribute("basePath", "/manager/orders");
        return "manager/orders";
    }

    @GetMapping("/manager/reviews")
    public String listReviews(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            Object[] items = HttpService.get("/reviews", Object[].class);
            model.addAttribute("items", items == null ? List.of() : List.of(items));
        } catch (Exception e) {
            model.addAttribute("items", List.of());
        }
        model.addAttribute("title", "Отзывы");
        model.addAttribute("entityName", "Отзывы");
        model.addAttribute("basePath", "/manager/reviews");
        return "manager/reviews";
    }

    @GetMapping("/manager/stocks")
    public String listStocks(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            Object[] items = HttpService.get("/stocks", Object[].class);
            model.addAttribute("items", items == null ? List.of() : List.of(items));
        } catch (Exception e) {
            model.addAttribute("items", List.of());
        }
        model.addAttribute("title", "Склад");
        model.addAttribute("entityName", "Склад");
        model.addAttribute("basePath", "/manager/stocks");
        return "manager/stocks";
    }

    @GetMapping("/manager/order-items")
    public String listOrderItems(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            Object[] items = HttpService.get("/order-items", Object[].class);
            model.addAttribute("items", items == null ? List.of() : List.of(items));
        } catch (Exception e) {
            model.addAttribute("items", List.of());
        }
        model.addAttribute("title", "Позиции заказа");
        model.addAttribute("entityName", "Позиции заказа");
        model.addAttribute("basePath", "/manager/order-items");
        return "manager/order-items";
    }

    @GetMapping("/manager/products/edit/{id}")
    public String editProduct(@PathVariable Long id, HttpSession session, Model model) {
        if (hasNoManagerRole(session)) {
            return "redirect:/";
        }
        try {
            if (id != null && id == 0L) {
                model.addAttribute("product", new Product());
            } else {
                Product p = HttpService.get("/products/" + id, Product.class);
                model.addAttribute("product", p);
            }
        } catch (Exception e) {
            model.addAttribute("product", new Product());
        }
        model.addAttribute("title", "Редактировать товар");
        return "manager/edit-product";
    }

    @PostMapping("/manager/products/save")
    public String saveProduct(HttpSession session,
                              @org.springframework.web.bind.annotation.RequestParam(required = false) Long id,
                              @org.springframework.web.bind.annotation.RequestParam String name,
                              @org.springframework.web.bind.annotation.RequestParam(required = false) String description,
                              @org.springframework.web.bind.annotation.RequestParam(required = false) Double price) {
        if (hasNoManagerRole(session)) {
            return "redirect:/";
        }
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("name", name);
            payload.put("description", description);
            payload.put("price", price);

            if (id != null) {
                HttpService.put("/products/" + id, payload, String.class);
            } else {
                HttpService.post("/products", payload, String.class);
            }
            return "redirect:/manager/products?success";
        } catch (Exception e) {
            return "redirect:/manager/products?error";
        }
    }

    @PostMapping("/manager/products/delete")
    public String deleteProduct(HttpSession session, @org.springframework.web.bind.annotation.RequestParam Long id) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            HttpService.delete("/products/" + id, String.class);
            return "redirect:/manager/products?deleted";
        } catch (Exception e) {
            return "redirect:/manager/products?error";
        }
    }

    @GetMapping("/manager/categories/create")
    public String createCategory(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        model.addAttribute("category", new com.strhzy.finalpr.model.Category());
        model.addAttribute("title", "Создать категорию");
        return "manager/edit-category";
    }

    @GetMapping("/manager/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            com.strhzy.finalpr.model.Category c = HttpService.get("/categories/" + id, com.strhzy.finalpr.model.Category.class);
            model.addAttribute("category", c);
        } catch (Exception e) {
            model.addAttribute("category", new com.strhzy.finalpr.model.Category());
        }
        model.addAttribute("title", "Редактировать категорию");
        return "manager/edit-category";
    }

    @PostMapping("/manager/categories/save")
    public String saveCategory(HttpSession session,
                               @org.springframework.web.bind.annotation.RequestParam(required = false) Long id,
                               @org.springframework.web.bind.annotation.RequestParam String name,
                               @org.springframework.web.bind.annotation.RequestParam(required = false) String description) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("name", name);
            payload.put("description", description);
            if (id != null) HttpService.put("/categories/" + id, payload, String.class);
            else HttpService.post("/categories", payload, String.class);
            return "redirect:/manager/categories?success";
        } catch (Exception e) {
            return "redirect:/manager/categories?error";
        }
    }

    @PostMapping("/manager/categories/delete")
    public String deleteCategory(HttpSession session, @org.springframework.web.bind.annotation.RequestParam Long id) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            HttpService.delete("/categories/" + id, String.class);
            return "redirect:/manager/categories?deleted";
        } catch (Exception e) {
            return "redirect:/manager/categories?error";
        }
    }

    @GetMapping("/manager/manufacturers/create")
    public String createManufacturer(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        model.addAttribute("manufacturer", new com.strhzy.finalpr.model.Manufacturer());
        model.addAttribute("title", "Создать производителя");
        return "manager/edit-manufacturer";
    }

    @GetMapping("/manager/manufacturers/edit/{id}")
    public String editManufacturer(@PathVariable Long id, HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            com.strhzy.finalpr.model.Manufacturer m = HttpService.get("/manufacturers/" + id, com.strhzy.finalpr.model.Manufacturer.class);
            model.addAttribute("manufacturer", m);
        } catch (Exception e) {
            model.addAttribute("manufacturer", new com.strhzy.finalpr.model.Manufacturer());
        }
        model.addAttribute("title", "Редактировать производителя");
        return "manager/edit-manufacturer";
    }

    @PostMapping("/manager/manufacturers/save")
    public String saveManufacturer(HttpSession session,
                                   @org.springframework.web.bind.annotation.RequestParam(required = false) Long id,
                                   @org.springframework.web.bind.annotation.RequestParam String name,
                                   @org.springframework.web.bind.annotation.RequestParam(required = false) String description,
                                   @org.springframework.web.bind.annotation.RequestParam(required = false) String country) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("name", name);
            payload.put("description", description);
            payload.put("country", country);
            if (id != null) HttpService.put("/manufacturers/" + id, payload, String.class);
            else HttpService.post("/manufacturers", payload, String.class);
            return "redirect:/manager/manufacturers?success";
        } catch (Exception e) {
            return "redirect:/manager/manufacturers?error";
        }
    }

    @PostMapping("/manager/manufacturers/delete")
    public String deleteManufacturer(HttpSession session, @org.springframework.web.bind.annotation.RequestParam Long id) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            HttpService.delete("/manufacturers/" + id, String.class);
            return "redirect:/manager/manufacturers?deleted";
        } catch (Exception e) {
            return "redirect:/manager/manufacturers?error";
        }
    }

    @GetMapping("/manager/orders/create")
    public String createOrder(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        model.addAttribute("order", new com.strhzy.finalpr.model.Order());
        try {
            Customer[] customers = HttpService.get("/customers", Customer[].class);
            model.addAttribute("customers", customers == null ? List.of() : List.of(customers));
        } catch (Exception e) {
            model.addAttribute("customers", List.of());
        }
        model.addAttribute("title", "Создать заказ");
        return "manager/edit-order";
    }

    @GetMapping("/manager/orders/edit/{id}")
    public String editOrder(@PathVariable Long id, HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            com.strhzy.finalpr.model.Order o = HttpService.get("/orders/" + id, com.strhzy.finalpr.model.Order.class);
            model.addAttribute("order", o);
        } catch (Exception e) {
            model.addAttribute("order", new com.strhzy.finalpr.model.Order());
        }
        try {
            Customer[] customers = HttpService.get("/customers", Customer[].class);
            model.addAttribute("customers", customers == null ? List.of() : List.of(customers));
        } catch (Exception e) {
            model.addAttribute("customers", List.of());
        }
        model.addAttribute("title", "Редактировать заказ");
        return "manager/edit-order";
    }

    @PostMapping("/manager/orders/save")
    public String saveOrder(HttpSession session,
                            @org.springframework.web.bind.annotation.RequestParam(required = false) Long id,
                            @org.springframework.web.bind.annotation.RequestParam Long customerId,
                            @org.springframework.web.bind.annotation.RequestParam(required = false) String orderDate,
                            @org.springframework.web.bind.annotation.RequestParam(required = false) String status,
                            @org.springframework.web.bind.annotation.RequestParam(required = false) Double totalAmount) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("customerId", customerId);
            if (orderDate != null) payload.put("orderDate", orderDate);
            payload.put("status", status);
            payload.put("totalAmount", totalAmount);
            if (id != null) HttpService.put("/orders/" + id, payload, String.class);
            else HttpService.post("/orders", payload, String.class);
            return "redirect:/manager/orders?success";
        } catch (Exception e) {
            return "redirect:/manager/orders?error";
        }
    }

    @PostMapping("/manager/orders/delete")
    public String deleteOrder(HttpSession session, @org.springframework.web.bind.annotation.RequestParam Long id) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            HttpService.delete("/orders/" + id, String.class);
            return "redirect:/manager/orders?deleted";
        } catch (Exception e) {
            return "redirect:/manager/orders?error";
        }
    }

    @GetMapping("/manager/order-items/create")
    public String createOrderItem(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        model.addAttribute("item", new com.strhzy.finalpr.model.OrderItem());
        try {
            Order[] orders = HttpService.get("/orders", Order[].class);
            model.addAttribute("orders", orders == null ? List.of() : List.of(orders));
        } catch (Exception e) {
            model.addAttribute("orders", List.of());
        }
        try {
            Product[] products = HttpService.get("/products", Product[].class);
            model.addAttribute("products", products == null ? List.of() : List.of(products));
        } catch (Exception e) {
            model.addAttribute("products", List.of());
        }
        model.addAttribute("title", "Создать позицию заказа");
        return "manager/edit-order-item";
    }

    @GetMapping("/manager/order-items/edit/{id}")
    public String editOrderItem(@PathVariable Long id, HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            com.strhzy.finalpr.model.OrderItem oi = HttpService.get("/order-items/" + id, com.strhzy.finalpr.model.OrderItem.class);
            model.addAttribute("item", oi);
        } catch (Exception e) {
            model.addAttribute("item", new com.strhzy.finalpr.model.OrderItem());
        }
        try {
            Order[] orders = HttpService.get("/orders", Order[].class);
            model.addAttribute("orders", orders == null ? List.of() : List.of(orders));
        } catch (Exception e) {
            model.addAttribute("orders", List.of());
        }
        try {
            Product[] products = HttpService.get("/products", Product[].class);
            model.addAttribute("products", products == null ? List.of() : List.of(products));
        } catch (Exception e) {
            model.addAttribute("products", List.of());
        }
        model.addAttribute("title", "Редактировать позицию заказа");
        return "manager/edit-order-item";
    }

    @PostMapping("/manager/order-items/save")
    public String saveOrderItem(HttpSession session,
                                @org.springframework.web.bind.annotation.RequestParam(required = false) Long id,
                                @org.springframework.web.bind.annotation.RequestParam Long orderId,
                                @org.springframework.web.bind.annotation.RequestParam Long productId,
                                @org.springframework.web.bind.annotation.RequestParam Integer quantity,
                                @org.springframework.web.bind.annotation.RequestParam Double price) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("orderId", orderId);
            payload.put("productId", productId);
            payload.put("quantity", quantity);
            payload.put("price", price);
            if (id != null) HttpService.put("/order-items/" + id, payload, String.class);
            else HttpService.post("/order-items", payload, String.class);
            return "redirect:/manager/order-items?success";
        } catch (Exception e) {
            return "redirect:/manager/order-items?error";
        }
    }

    @PostMapping("/manager/order-items/delete")
    public String deleteOrderItem(HttpSession session, @org.springframework.web.bind.annotation.RequestParam Long id) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            HttpService.delete("/order-items/" + id, String.class);
            return "redirect:/manager/order-items?deleted";
        } catch (Exception e) {
            return "redirect:/manager/order-items?error";
        }
    }

    @GetMapping("/manager/reviews/create")
    public String createReview(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        model.addAttribute("review", new com.strhzy.finalpr.model.Review());
        try {
            Product[] products = HttpService.get("/products", Product[].class);
            model.addAttribute("products", products == null ? List.of() : List.of(products));
        } catch (Exception e) {
            model.addAttribute("products", List.of());
        }
        try {
            Customer[] customers = HttpService.get("/customers", Customer[].class);
            model.addAttribute("customers", customers == null ? List.of() : List.of(customers));
        } catch (Exception e) {
            model.addAttribute("customers", List.of());
        }
        model.addAttribute("title", "Создать отзыв");
        return "manager/edit-review";
    }

    @GetMapping("/manager/reviews/edit/{id}")
    public String editReview(@PathVariable Long id, HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            com.strhzy.finalpr.model.Review r = HttpService.get("/reviews/" + id, com.strhzy.finalpr.model.Review.class);
            model.addAttribute("review", r);
        } catch (Exception e) {
            model.addAttribute("review", new com.strhzy.finalpr.model.Review());
        }
        try {
            Product[] products = HttpService.get("/products", Product[].class);
            model.addAttribute("products", products == null ? List.of() : List.of(products));
        } catch (Exception e) {
            model.addAttribute("products", List.of());
        }
        try {
            Customer[] customers = HttpService.get("/customers", Customer[].class);
            model.addAttribute("customers", customers == null ? List.of() : List.of(customers));
        } catch (Exception e) {
            model.addAttribute("customers", List.of());
        }
        model.addAttribute("title", "Редактировать отзыв");
        return "manager/edit-review";
    }

    @PostMapping("/manager/reviews/save")
    public String saveReview(HttpSession session,
                             @org.springframework.web.bind.annotation.RequestParam(required = false) Long id,
                             @org.springframework.web.bind.annotation.RequestParam Long productId,
                             @org.springframework.web.bind.annotation.RequestParam Long customerId,
                             @org.springframework.web.bind.annotation.RequestParam Integer rating,
                             @org.springframework.web.bind.annotation.RequestParam String comment) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("productId", productId);
            payload.put("customerId", customerId);
            payload.put("rating", rating);
            payload.put("comment", comment);
            if (id != null) HttpService.put("/reviews/" + id, payload, String.class);
            else HttpService.post("/reviews", payload, String.class);
            return "redirect:/manager/reviews?success";
        } catch (Exception e) {
            return "redirect:/manager/reviews?error";
        }
    }

    @PostMapping("/manager/reviews/delete")
    public String deleteReview(HttpSession session, @org.springframework.web.bind.annotation.RequestParam Long id) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            HttpService.delete("/reviews/" + id, String.class);
            return "redirect:/manager/reviews?deleted";
        } catch (Exception e) {
            return "redirect:/manager/reviews?error";
        }
    }

    @GetMapping("/manager/stocks/create")
    public String createStock(HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        model.addAttribute("stock", new com.strhzy.finalpr.model.Stock());
        try {
            Product[] products = HttpService.get("/products", Product[].class);
            model.addAttribute("products", products == null ? List.of() : List.of(products));
        } catch (Exception e) {
            model.addAttribute("products", List.of());
        }
        model.addAttribute("title", "Создать складскую позицию");
        return "manager/edit-stock";
    }

    @GetMapping("/manager/stocks/edit/{id}")
    public String editStock(@PathVariable Long id, HttpSession session, Model model) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            com.strhzy.finalpr.model.Stock s = HttpService.get("/stocks/" + id, com.strhzy.finalpr.model.Stock.class);
            model.addAttribute("stock", s);
        } catch (Exception e) {
            model.addAttribute("stock", new com.strhzy.finalpr.model.Stock());
        }
        try {
            Product[] products = HttpService.get("/products", Product[].class);
            model.addAttribute("products", products == null ? List.of() : List.of(products));
        } catch (Exception e) {
            model.addAttribute("products", List.of());
        }
        model.addAttribute("title", "Редактировать складскую позицию");
        return "manager/edit-stock";
    }

    @PostMapping("/manager/stocks/save")
    public String saveStock(HttpSession session,
                            @org.springframework.web.bind.annotation.RequestParam(required = false) Long id,
                            @org.springframework.web.bind.annotation.RequestParam Long productId,
                            @org.springframework.web.bind.annotation.RequestParam Integer quantity) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("productId", productId);
            payload.put("quantity", quantity);
            if (id != null) HttpService.put("/stocks/" + id, payload, String.class);
            else HttpService.post("/stocks", payload, String.class);
            return "redirect:/manager/stocks?success";
        } catch (Exception e) {
            return "redirect:/manager/stocks?error";
        }
    }

    @PostMapping("/manager/stocks/delete")
    public String deleteStock(HttpSession session, @org.springframework.web.bind.annotation.RequestParam Long id) {
        if (hasNoManagerRole(session)) return "redirect:/";
        try {
            HttpService.delete("/stocks/" + id, String.class);
            return "redirect:/manager/stocks?deleted";
        } catch (Exception e) {
            return "redirect:/manager/stocks?error";
        }
    }
}
