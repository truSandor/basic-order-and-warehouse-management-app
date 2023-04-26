package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.ProductDto;
import com.orderandwarehouse.app.repository.ProductDao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;

    public List<Product> getAll() {
        return productDao.findAllByVisibleTrueOrderByNameAscVersionAscIdAsc();
    }

    public Optional<Product> getById(Long id) {
        return productDao.findByIdAndVisibleTrue(id);
    }

    public Product add(@Valid Product product) {
        return productDao.save(product);
    }

    public Product update(Long id, ProductDto dto) {
        Product product = productDao.findByIdAndVisibleTrue(id).orElseThrow(NoSuchElementException::new);
        product.setName(dto.getName());
        product.setVersion(dto.getVersion());
        product.setDimensions(dto.getDimensions());
        product.setWeightInGrammes(dto.getWeightInGrammes());
        return productDao.save(product);
    }
}
