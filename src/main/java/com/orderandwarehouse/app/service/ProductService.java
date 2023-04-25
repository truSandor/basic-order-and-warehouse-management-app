package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.repository.PartsListRowDao;
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
    private final PartsListRowDao partsListRowDao;

    public List<Product> getAll() {
        return productDao.findAllByVisibleTrueOrderByNameAscVersionAscIdAsc();
    }

    public Optional<Product> getById(Long id) {
        return productDao.findByIdAndVisibleTrue(id);
    }

    public Product add(@Valid Product product) {
        return productDao.save(product);
    }

    public Product update(Long id, @Valid Product product) {
        Product productFromDb = productDao.findByIdAndVisibleTrue(id).orElseThrow(NoSuchElementException::new);
        product.setId(productFromDb.getId());
        //todo rework all updates: converter not needed, just get enitity form db, and updates fields changed in dto,
        // then save the entity
        return productDao.save(productFromDb);
    }
}
