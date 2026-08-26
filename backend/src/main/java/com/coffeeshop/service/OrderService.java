package com.coffeeshop.service;

import com.coffeeshop.dto.OrderRequest;
import com.coffeeshop.exception.ResourceNotFoundException;
import com.coffeeshop.model.Order;
import com.coffeeshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private static final Set<String> VALID_STATUSES = Set.of(
            "PENDING", "CONFIRMED", "COMPLETED", "CANCELLED"
    );

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setItems(request.getItems());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus("PENDING");
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    public Order updateOrderStatus(Long id, String status) {
        String upperStatus = status.toUpperCase();
        if (!VALID_STATUSES.contains(upperStatus)) {
            throw new IllegalArgumentException(
                    "Invalid status. Must be one of: " + String.join(", ", VALID_STATUSES));
        }

        Order order = getOrderById(id);
        order.setStatus(upperStatus);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status.toUpperCase());
    }
}
