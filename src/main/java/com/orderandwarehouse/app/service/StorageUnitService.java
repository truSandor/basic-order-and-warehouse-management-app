package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.exception.StorageUnitStillInUseException;
import com.orderandwarehouse.app.model.Component;
import com.orderandwarehouse.app.model.StorageUnit;
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
public class StorageUnitService {
    private final StorageUnitDao storageUnitDao;

    public List<StorageUnit> getAll() {
        return storageUnitDao.findAllByVisibleTrueOrderByIdAsc();
    }

    public Optional<StorageUnit> getById(Long id) {
        return storageUnitDao.findById(id);
    }

    public StorageUnit add(@Valid StorageUnit storageUnit) {
        return storageUnitDao.save(storageUnit);
    }


    public StorageUnit update(Long id, @Valid StorageUnit storageUnit) {
        StorageUnit storageUnitFromDb = storageUnitDao.findByIdAndVisibleTrue(id).orElseThrow(NoSuchElementException::new);
        storageUnit.setId(storageUnitFromDb.getId());
        storageUnit.setComponent(storageUnitFromDb.getComponent());
        return storageUnitDao.save(storageUnit);
    }

    public boolean softDelete(Long id) throws SQLException {
        Component component = storageUnitDao.findById(id)
                .map(StorageUnit::getComponent)
                .orElseThrow(() -> new NoSuchElementException(String.format("Component '%d' not found!", id)));
        if (component == null) {
            Integer linesModifiedCount = storageUnitDao.setInvisibleById(id);
            switch (linesModifiedCount) {
                case 0 -> throw new NoSuchElementException(String.format("Storage unit '%d' not found!", id));
                case 1 -> {
                    return true;
                }
                //should never happen, because IDs are unique
                default ->
                        throw new SQLException(String.format("Error multiple storage unit with the same ID '%d' updated!", id));
            }
        } else {
            throw new StorageUnitStillInUseException(
                    String.format(
                            "Component '%d' found in storage unit '%d'. Empty the storage unit before deleting!",
                            id,
                            component.getId()));
        }
    }

    public List<StorageUnit> getAllByComponentId(Long componentId) {
        return storageUnitDao.findAllByVisibleTrueAndComponent_Id(componentId);
    }
}
