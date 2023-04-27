package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.PartsListRow;
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

    public List<PartsListRow> getPartsListByProductId(Long productId) {
        return partsListRowDao.findAllByProductId(productId);
    }

    public PartsListRow add(PartsListRow partsListRow) {
        return partsListRowDao.save(partsListRow);
    }

    public List<PartsListRow> addAllToProduct(Long productId, List<PartsListRow> partsList) {
        checkIfAllPartsListRowsHaveTheGivenProductId(productId, partsList);
        /*todo new PartsListRows shouldn't have ids, because if they have id then they will be updated
                or maybe there should be just 1 method for both add and update
         */
        return partsListRowDao.saveAll(partsList);
    }

    public List<PartsListRow> updateAllBelongingToProduct(Long productId, Set<PartsListRow> partsList) {
        checkIfAllPartsListRowsHaveTheGivenProductId(productId, partsList);
        return partsListRowDao.saveAll(partsList);
    }

    public void delete(Long id){
        partsListRowDao.deleteById(id);
    }

    public void deleteAllByProductId(Long productId){
        partsListRowDao.deleteByProduct_Id(productId);
    }

    private static void checkIfAllPartsListRowsHaveTheGivenProductId(Long productId, Collection<PartsListRow> partsList) {
        if (partsList.stream().anyMatch(plr -> !plr.getProduct().getId().equals(productId)))
            throw new IllegalArgumentException("Not all parts list rows have the same product id!");
    }
}
