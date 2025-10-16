package com.burgerstream.backend.service.order;

import com.burgerstream.backend.exception.InvalidOrderException;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.order.Order;
import com.burgerstream.backend.model.order.OrderItem;
import com.burgerstream.backend.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order){
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new InvalidOrderException("Cannot create order with no items.");
        }
        for (OrderItem item : order.getOrderItems()){
            item.setOrder(order);
        }

        return this.orderRepository.save(order);
    }

    public Order getOrder(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order with id: " + id + " does not exist"));
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order updateCostumerDetails(Long id, Order newCustomerDetails){
        Order oldOrderDetails = orderRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Order with id: " + id + " does not exist"));

        oldOrderDetails.setCustomerName(newCustomerDetails.getCustomerName());
        oldOrderDetails.setCustomerEmail(newCustomerDetails.getCustomerEmail());

        return orderRepository.save(oldOrderDetails);
    }

    public Map<String, Boolean> deleteOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Order with id: " + id + " does not exist"));

        orderRepository.delete(order);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);

        return response;
    }

    public List<OrderItem> getOrderItemsFromOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Order with id: " + id + " does not exist"));
        return order.getOrderItems();
    }
}