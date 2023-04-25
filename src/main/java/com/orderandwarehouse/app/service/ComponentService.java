package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.exception.ComponentStillInUseException;
import com.orderandwarehouse.app.model.*;
import com.orderandwarehouse.app.repository.ComponentDao;
import com.orderandwarehouse.app.repository.PartsListRowDao;
import com.orderandwarehouse.app.repository.ProductDao;
import com.orderandwarehouse.app.repository.StorageUnitDao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComponentService {
    private final ComponentDao componentDao;
    private final StorageUnitDao storageUnitDao;
    private final PartsListRowDao partsListRowDao;
    private final ProductDao productDao;

    public List<Component> getAll() {
        return componentDao.findAllByVisibleTrueOrderByIdAsc();
    }

    public Optional<Component> getById(Long id) {
        return componentDao.findByIdAndVisibleTrue(id);
    }

    public Component add(@Valid Component component) {
        return componentDao.save(component);
    }

    public Component update(Long id, @Valid Component component) {
        Component componentFromDb = componentDao.findByIdAndVisibleTrue(id).orElseThrow(NoSuchElementException::new);
        component.setId(componentFromDb.getId());
        component.setPartsListRows(componentFromDb.getPartsListRows());
        component.setStorageUnits(componentFromDb.getStorageUnits());
        return componentDao.save(component);
    }

    public boolean softDelete(Long id) throws SQLException {
        List<StorageUnit> storageUnits = storageUnitDao.findAllByVisibleTrueAndComponent_Id(id);
        List<PartsListRow> partsListRows = partsListRowDao.findAllByVisibleTrueAndComponent_Id(id);
        if (storageUnits.isEmpty() && partsListRows.isEmpty()) {
            Integer linesModifiedCount = componentDao.setInvisibleById(id);
            switch (linesModifiedCount) {
                case 0 -> throw new NoSuchElementException(String.format("Component '%d' not found!", id));
                case 1 -> {
                    return true;
                }
                //should never happen, because IDs are unique
                default ->
                        throw new SQLException(String.format("Error multiple component with the same ID '%d' updated!", id));
            }
        } else {
            throw new ComponentStillInUseException(createExceptionMessage(id, partsListRows, storageUnits));
        }
    }


    private String createExceptionMessage(Long id, List<PartsListRow> partsListRows, List<StorageUnit> storageUnits) {
        return String.format(
                "Component '%d' found in parts list(s) of product(s): %s%sRemove component from parts list(s) first!%s" +
                        "and/or%s" +
                        "Component '%d' found in storage units: %s%sEmpty the storage units first!",
                id,
                getProductIds(partsListRows),
                System.lineSeparator(),
                System.lineSeparator(),
                System.lineSeparator(),
                id,
                getStorageUnitIds(storageUnits),
                System.lineSeparator()
        );
    }

    private List<Long> getStorageUnitIds(List<StorageUnit> storageUnits) {
        return storageUnits.stream().map(StorageUnit::getId).toList();
    }

    private List<Long> getProductIds(List<PartsListRow> partsListRows) {
        return partsListRows.stream().map(plr -> plr.getProduct().getId()).distinct().toList();
    }

    public List<Component> getByNameLike(String nameLike) {
        return componentDao.findAllByNameIsContainingIgnoreCaseAndVisibleTrue(nameLike);
    }
}
