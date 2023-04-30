package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.converter.OrderConverter;
import com.orderandwarehouse.app.exception.OrderInProgressException;
import com.orderandwarehouse.app.model.Order;
import com.orderandwarehouse.app.model.dto.OrderDto;
import com.orderandwarehouse.app.repository.OrderDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDao orderDao;
    private final OrderConverter converter;

    public List<Order> getAll() {
        return orderDao.findAllByIdNotNullOrderByDateReceived();
    }

    public Optional<Order> getById(Long id) {
        return orderDao.findById(id);
    }

    public List<Order> getByProductNameLike(String nameLike) {
        return orderDao.findAllByProduct_NameIsContainingIgnoreCase(nameLike);
    }

    public Order add(OrderDto dto) {
        return orderDao.save(converter.dtoToEntityForAdding(dto));
    }

    public Order update(Long id, OrderDto dto) {
        return orderDao.save(converter.dtoToEntityForUpdating(dto));
    }

    public void delete(Long id) {
        Order order = orderDao.findById(id).orElseThrow(NoSuchElementException::new);
        if (order.isActive()) throw new OrderInProgressException(id, order.getStatus());
        orderDao.deleteById(id);
    }
}
