package com.burgerstream.backend.controller.order;

import com.burgerstream.backend.model.order.Order;
import com.burgerstream.backend.model.order.OrderItem;
import com.burgerstream.backend.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/BurgerStream/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) { return orderService.createOrder(order);}


    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    // As the order entity is after a person has ordered, they cannot change much about the order (fx the food or time of date.)
    // So this update is to update customer information, should they have given the wrong email or something.
    // This might change in the future to be handled in a Customer model instead, but for now this project focuses on a restaurant that is not interested in customer data besides name and contact info

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrderCustomerDetails(@PathVariable Long id, @RequestBody Order newCustomerDetails){
        return ResponseEntity.ok(orderService.updateCostumerDetails(id, newCustomerDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteOrder(@PathVariable Long id){
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

    @GetMapping("/{id}/items")
    public List<OrderItem> getOrderItemsFromOrder(@PathVariable Long id){
        return orderService.getOrderItemsFromOrder(id);
    }
}