package com.coffeeshop.repository;

import com.coffeeshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByCustomerEmailOrderByCreatedAtDesc(String customerEmail);
    List<Order> findAllByOrderByCreatedAtDesc();
}
