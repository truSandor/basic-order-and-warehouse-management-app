package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.converter.PartsListRowConverter;
import com.orderandwarehouse.app.exception.ProductStillInUseException;
import com.orderandwarehouse.app.model.PartsListRow;
import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.PartsListRowDto;
import com.orderandwarehouse.app.repository.PartsListRowDao;
import com.orderandwarehouse.app.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PartsListRowService {
    private final PartsListRowDao partsListRowDao;
    private final PartsListRowConverter converter;
    private final ProductDao productDao;

    public List<PartsListRow> getPartsListByProductId(Long productId) {
        if (!productDao.existsById(productId))
            throw new NoSuchElementException(String.format("Product '%d' not found!", productId));
        return partsListRowDao.findAllByProductId(productId);
    }

    public PartsListRow add(PartsListRowDto dto) {
        return partsListRowDao.save(converter.dtoToEntityForAdding(dto));
    }

    public List<PartsListRow> addAllToProduct(Long productId, List<PartsListRowDto> dtoPartsList) {
        checkIfAllPartsListRowsHaveTheGivenProductId(productId, dtoPartsList);
        List<PartsListRow> partsList = dtoPartsList.stream().map(converter::dtoToEntityForAdding).toList();
        partsList.forEach(this::checkIfProductHasActiveOrder);
        return partsListRowDao.saveAll(partsList);
    }

    public List<PartsListRow> updateAllBelongingToProduct(Long productId, Set<PartsListRowDto> dtoPartsList) {
        checkIfAllPartsListRowsHaveTheGivenProductId(productId, dtoPartsList);
        List<PartsListRow> partsList = dtoPartsList.stream().map(converter::dtoToEntityForUpdating).toList();
        return partsListRowDao.saveAll(partsList);
    }

    public void delete(Long id) {
        PartsListRow partsListRow = partsListRowDao.findById(id).orElseThrow();
        checkIfProductHasActiveOrder(partsListRow);
        partsListRowDao.deleteById(id);
    }

    private void checkIfProductHasActiveOrder(PartsListRow partsListRow) {
        if (partsListRow.getProduct().hasActiveOrders())
            throw new ProductStillInUseException(
                    partsListRow.getProduct().getId(),
                    partsListRow.getProduct().getActiveOrderIds(),
                    true);
    }

    public void deleteAllByProductId(Long productId) {
        Product product = getProductById(productId);
        if (product.hasActiveOrders())
            throw new ProductStillInUseException(productId, product.getActiveOrderIds(), true);
        partsListRowDao.deleteByProduct_Id(productId);
    }

    private Product getProductById(Long productId) {
        return partsListRowDao.findFirstByProductId(productId).orElseThrow().getProduct();
    }

    private void checkIfAllPartsListRowsHaveTheGivenProductId(Long productId, Collection<PartsListRowDto> partsList) {
        if (partsList.stream().anyMatch(plr -> !plr.getProductId().equals(productId)))
            throw new InputMismatchException("Not all parts list rows have the same product id, Or id in path is different!");
    }
}
