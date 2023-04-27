package com.orderandwarehouse.app.converter;

import com.orderandwarehouse.app.model.Order;
import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.OrderDto;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class OrderConverter {
    private final ProductDao productDao;

    public Order dtoToEntity(OrderDto dto) {
        Order entity = new Order();
        Product product = productDao.findById(dto.getProductId()).orElseThrow(NoSuchElementException::new);
        entity.setProduct(product);
        entity.setQuantity(dto.getQuantity());
        entity.setDateReceived(dto.getDateReceived());
        entity.setDateStarted(dto.getDateStarted());
        entity.setDateCompleted(dto.getDateCompleted());
        entity.setDeadline(dto.getDeadline());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
