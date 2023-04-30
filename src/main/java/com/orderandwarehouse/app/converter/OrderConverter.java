package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.exception.NoIdException;
import com.orderandwarehouse.app.model.Order;
import com.orderandwarehouse.app.model.dto.OrderDto;
import com.orderandwarehouse.app.repository.OrderDao;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderConverter {
    private final ProductDao productDao;
    private final OrderDao orderDao;

    public Order dtoToEntityForAdding(OrderDto dto) {
        Order entity = new Order();
        entity.setProduct(productDao.findById(dto.getProductId()).orElseThrow());
        entity.setQuantity(dto.getQuantity());
        entity.setDateReceived(dto.getDateReceived());
        entity.setDateStarted(dto.getDateStarted());
        entity.setDateCompleted(dto.getDateCompleted());
        entity.setDeadline(dto.getDeadline());
        entity.setDateAdded(LocalDateTime.now());
        entity.setDateModified(LocalDateTime.now());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public Order dtoToEntityForUpdating(OrderDto dto) {
        if (dto.getId() == null) throw new NoIdException(OrderDto.class); //should never be true
        Order entity = orderDao.findById(dto.getId()).orElseThrow();
        entity.setQuantity(dto.getQuantity());
        entity.setDateStarted(dto.getDateStarted());
        entity.setDateCompleted(dto.getDateCompleted());
        entity.setDeadline(dto.getDeadline());
        entity.setDateModified(LocalDateTime.now());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
