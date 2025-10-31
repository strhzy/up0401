package com.strhzy.api.controller;

import com.strhzy.api.dto.OrderItemDto;
import com.strhzy.api.exception.ResourceNotFoundException;
import com.strhzy.api.model.Order;
import com.strhzy.api.model.OrderItem;
import com.strhzy.api.model.Product;
import com.strhzy.api.repository.OrderRepository;
import com.strhzy.api.repository.ProductRepository;
import com.strhzy.api.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-items")
@Tag(name = "Order Item Controller", description = "Операции с позициями заказов")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderItemController(OrderItemService orderItemService,
                               OrderRepository orderRepository,
                               ProductRepository productRepository) {
        this.orderItemService = orderItemService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    @Operation(summary = "Получить все позиции заказов")
    public List<OrderItemDto> getAllOrderItems() {
        return orderItemService.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить позицию заказа по ID")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable Long id) {
        return orderItemService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новую позицию заказа")
    public ResponseEntity<OrderItemDto> createOrderItem(@Valid @RequestBody OrderItemDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + dto.getOrderId() + " не найден"));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + dto.getProductId() + " не найден"));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());

        OrderItem saved = orderItemService.save(item);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить позицию заказа")
    public ResponseEntity<OrderItemDto> updateOrderItem(@PathVariable Long id, @Valid @RequestBody OrderItemDto dto) {
        return orderItemService.findById(id)
                .map(existing -> {
                    Order order = orderRepository.findById(dto.getOrderId())
                            .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + dto.getOrderId() + " не найден"));
                    Product product = productRepository.findById(dto.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Товар с ID " + dto.getProductId() + " не найден"));

                    existing.setOrder(order);
                    existing.setProduct(product);
                    existing.setQuantity(dto.getQuantity());
                    existing.setPrice(dto.getPrice());
                    OrderItem saved = orderItemService.save(existing);
                    return ResponseEntity.ok(toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить позицию заказа")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        return orderItemService.findById(id)
                .map(item -> {
                    orderItemService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private OrderItemDto toDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setOrderId(item.getOrder() != null ? item.getOrder().getId() : null);
        dto.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}
