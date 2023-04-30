package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.converter.ProductConverter;
import com.orderandwarehouse.app.exception.ProductStillInUseException;
import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.ProductDto;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;
    private final ProductConverter converter;

    public List<Product> getAll() {
        return productDao.findAllByIdNotNullOrderByNameAscVersionAsc();
    }

    public Optional<Product> getById(Long id) {
        return productDao.findById(id);
    }

    public Product add(ProductDto dto) {
        return productDao.save(converter.dtoToEntityForAdding(dto));
    }

    public Product update(ProductDto dto) {
        return productDao.save(converter.dtoToEntityForUpdating(dto));
    }

    public void delete(Long id) {
        Product product = productDao.findById(id).orElseThrow(NoSuchElementException::new);
        if (product.isInUse())
            throw new ProductStillInUseException(id, product.getActiveOrderIds(), product.hasPartsList());
        productDao.deleteById(id);
    }

    public List<Product> getByNameLike(String nameLike) {
        return productDao.findAllByNameContainingIgnoreCase(nameLike);
    }
}
