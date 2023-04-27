package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.Order;
import com.orderandwarehouse.app.repository.OrderDao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDao orderDao;

    public List<Order> getAll() {
        return orderDao.findAllByIdNotNullOrderByDateReceived();
    }

    public Optional<Order> getById(Long id) {
        return orderDao.findById(id);
    }

    public List<Order> getByNameLike(String nameLike) {
        return orderDao.findAllByProduct_NameIsContainingIgnoreCase(nameLike);
    }

    public Order add(@Valid Order order) {
        return orderDao.save(order);
    }

    public Order update(Long id, @Valid Order order) {
        Order orderFromDb = orderDao.findById(id).orElseThrow(NoSuchElementException::new);
        order.setId(orderFromDb.getId()); //todo check if it updates the original, or tries to create a new line with same id --> fails
        return orderDao.save(order);
    }

    public void delete(Long id) {
        orderDao.deleteById(id);
    }
}
