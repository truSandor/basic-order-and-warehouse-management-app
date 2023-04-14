package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.repository.OrderDao;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDao orderDao;
    private final ProductDao productDao;

}
