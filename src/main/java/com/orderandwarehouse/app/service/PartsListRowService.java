package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.converter.PartsListRowConverter;
import com.orderandwarehouse.app.exception.ProductStillInUseException;
import com.orderandwarehouse.app.model.PartsListRow;
import com.orderandwarehouse.app.model.Product;
import com.orderandwarehouse.app.model.dto.PartsListRowDto;
import com.orderandwarehouse.app.repository.PartsListRowDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PartsListRowService {
    private final PartsListRowDao partsListRowDao;
    private final PartsListRowConverter converter;

    public List<PartsListRow> getPartsListByProductId(Long productId) {
        return partsListRowDao.findAllByProductId(productId);
    }

    public PartsListRow add(PartsListRowDto dto) {
        return partsListRowDao.save(converter.dtoToEntityForAdding(dto));
    }

    public List<PartsListRow> addAllToProduct(Long productId, List<PartsListRowDto> dtoPartsList) {
        List<PartsListRow> partsList = dtoPartsList.stream().map(converter::dtoToEntityForAdding).toList();
        checkIfAllPartsListRowsHaveTheGivenProductId(productId, partsList);
        return partsListRowDao.saveAll(partsList);
    }

    public List<PartsListRow> updateAllBelongingToProduct(Long productId, Set<PartsListRowDto> dtoPartsList) {
        List<PartsListRow> partsList = dtoPartsList.stream().map(converter::dtoToEntityForUpdating).toList();
        checkIfAllPartsListRowsHaveTheGivenProductId(productId, partsList);
        return partsListRowDao.saveAll(partsList);
    }

    public void delete(Long id) {
        PartsListRow partsListRow = partsListRowDao.findById(id).orElseThrow();
        if (partsListRow.getProduct().hasActiveOrders())
            throw new ProductStillInUseException(
                    partsListRow.getProduct().getId(),
                    partsListRow.getProduct().getActiveOrderIds(),
                    true);
        partsListRowDao.deleteById(id);
    }

    public void deleteAllByProductId(Long productId) {
        Product product = getProductById(productId);
        if (product.hasActiveOrders()) throw new ProductStillInUseException(productId, product.getActiveOrderIds(), true);
        partsListRowDao.deleteByProduct_Id(productId);
    }

    private Product getProductById(Long productId) {
        return partsListRowDao.findFirstByProductId(productId).orElseThrow().getProduct();
    }

    private void checkIfAllPartsListRowsHaveTheGivenProductId(Long productId, Collection<PartsListRow> partsList) {
        if (partsList.stream().anyMatch(plr -> !plr.getProduct().getId().equals(productId)))
            throw new IllegalArgumentException("Not all parts list rows have the same product id, Or id in path is different!");
    }
}
