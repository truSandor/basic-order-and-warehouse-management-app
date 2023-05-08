package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Order;
import com.orderandwarehouse.app.model.dto.OrderDto;
import com.orderandwarehouse.app.repository.OrderDao;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class OrderConverter {
    private final ProductDao productDao;
    private final OrderDao orderDao;

    public Order dtoToEntityForAdding(OrderDto dto) {
        Order entity = new Order();
        entity.setProduct(productDao.findById(dto.getProductId()).orElseThrow());
        setAttributes(dto, entity);
        entity.setDateAdded(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setDateReceived(dto.getDateReceived());
        return entity;
    }

    public Order dtoToEntityForUpdating(OrderDto dto) {
        Order entity = orderDao.findById(dto.getId()).orElseThrow();
        setAttributes(dto, entity);
        return entity;
    }

    private static void setAttributes(OrderDto dto, Order entity) {
        entity.setQuantity(dto.getQuantity());
        entity.setDateStarted(dto.getDateStarted());
        entity.setDateCompleted(dto.getDateCompleted());
        entity.setDeadline(dto.getDeadline());
        entity.setDateModified(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        entity.setStatus(dto.getStatus());
    }
}
