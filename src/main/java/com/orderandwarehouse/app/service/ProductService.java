package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.repository.PartsListDao;
import com.orderandwarehouse.app.repository.PartsListRowDao;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;
    private final PartsListDao partsListDao;
    private final PartsListRowDao partsListRowDao;

    public List<Product> getAll() {
        return productDao.findAllByVisibleTrueOrderByNameAscVersionAscIdAsc();
    }
}
