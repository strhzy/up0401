package com.strhzy.api.controller;

import com.strhzy.api.dto.OrderDto;
import com.strhzy.api.dto.OrderItemDto;
import com.strhzy.api.exception.ResourceNotFoundException;
import com.strhzy.api.model.Order;
import com.strhzy.api.model.OrderItem;
import com.strhzy.api.model.Product;
import com.strhzy.api.model.Customer;
import com.strhzy.api.repository.OrderRepository;
import com.strhzy.api.repository.ProductRepository;
import com.strhzy.api.repository.CustomerRepository;
import com.strhzy.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Controller", description = "Операции с заказами")
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderService orderService,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository,
                           OrderRepository orderRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    @Operation(summary = "Получить все заказы")
    public List<OrderDto> getAllOrders(@RequestParam(required = false) Long customerId) {
        List<Order> orders;
        if (customerId != null) {
            orders = orderService.findByCustomerId(customerId);
        } else {
            orders = orderService.findAll();
        }
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заказ по ID")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + id + " не найден"));
        return ResponseEntity.ok(toDto(order));
    }

    @PostMapping
    @Operation(summary = "Создать новый заказ")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Покупатель с ID " + dto.getCustomerId() + " не найден"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(dto.getStatus());

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;
        if (dto.getOrderItems() != null && !dto.getOrderItems().isEmpty()) {
            for (OrderItemDto itemDto : dto.getOrderItems()) {
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + itemDto.getProductId() + " не найден"));
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(itemDto.getQuantity());
                item.setPrice(itemDto.getPrice());
                items.add(item);
                total += item.getPrice() * item.getQuantity();
            }
            order.setOrderItems(items);
            order.setTotalAmount(total);
        } else {
            order.setOrderItems(items);
            order.setTotalAmount(dto.getTotalAmount());
        }

        Order saved = orderService.save(order);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить заказ")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDto dto) {
        Order existing = orderService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + id + " не найден"));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Покупатель с ID " + dto.getCustomerId() + " не найден"));

        existing.setCustomer(customer);
        existing.setOrderDate(dto.getOrderDate());
        existing.setStatus(dto.getStatus());

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;
        if (dto.getOrderItems() != null && !dto.getOrderItems().isEmpty()) {
            for (OrderItemDto itemDto : dto.getOrderItems()) {
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + itemDto.getProductId() + " не найден"));
                OrderItem item = new OrderItem();
                item.setOrder(existing);
                item.setProduct(product);
                item.setQuantity(itemDto.getQuantity());
                item.setPrice(itemDto.getPrice());
                items.add(item);
                total += item.getPrice() * item.getQuantity();
            }
            existing.setOrderItems(items);
            existing.setTotalAmount(total);
        } else {
            existing.setOrderItems(items);
            existing.setTotalAmount(dto.getTotalAmount());
        }

        Order saved = orderService.save(existing);
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + id + " не найден"));
        orderService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        if (order.getOrderItems() != null) {
            dto.setOrderItems(order.getOrderItems().stream().map(item -> {
                OrderItemDto idto = new OrderItemDto();
                idto.setId(item.getId());
                idto.setOrderId(order.getId());
                idto.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
                idto.setQuantity(item.getQuantity());
                idto.setPrice(item.getPrice());
                return idto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}
