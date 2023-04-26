package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.ProductDto;
import com.orderandwarehouse.app.repository.ProductDao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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

//    public boolean softDelete(Long id) throws SQLException {
//        Product product = productDao.findByIdAndVisibleTrue(id)
//                .orElseThrow(() -> new NoSuchElementException(String.format("Product '%d' not found!", id)));
//        if (product.getPartsList() != null && !product.getPartsList().isEmpty() )
//            throw new ProductHasPartsListException(String.format("Product '%d' has part!", id)));
//        if ((product.getPartsList() == null || product.getPartsList().isEmpty()) &&
//                (product.getOrders() == null || product.getOrders().isEmpty() || product.hasNoActiveOrders())) {
//            Integer linesModifiedCount = productDao.setInvisibleById(id);
//            if (linesModifiedCount == 1) {
//                return true;
//            } else {
//                //should never happen, because IDs are unique
//                throw new SQLException(String.format("Error multiple products with the same ID '%d' updated!", id));
//            }
//        } else {
//            throw new ProductStillInUseException(
//                    String.format(
//                            "!",
//                            id,
//                            product.getId()));
//        }
//    }
}
