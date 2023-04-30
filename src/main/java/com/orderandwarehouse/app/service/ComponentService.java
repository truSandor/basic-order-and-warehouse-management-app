package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.converter.ComponentConverter;
import com.orderandwarehouse.app.exception.ComponentStillInUseException;
import com.orderandwarehouse.app.model.*;
import com.orderandwarehouse.app.model.dto.ComponentDto;
import com.orderandwarehouse.app.repository.ComponentDao;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComponentService {
    private final ComponentDao componentDao;
    private final ComponentConverter converter;

    public List<Component> getAll() {
        return componentDao.findAll();
    }

    public Optional<Component> getById(Long id) {
        return componentDao.findById(id);
    }

    public Component add(ComponentDto dto) {
        return componentDao.save(converter.dtoToEntityForAdding(dto));
    }

    public Component update(ComponentDto dto) {
        return componentDao.save(converter.dtoToEntityForUpdating(dto));
    }

    public void delete(Long id) {
        Component component = componentDao.findById(id).orElseThrow();
        if (component.isInUse())
            throw new ComponentStillInUseException(id, component.getStorageUnitIds(), component.getProductIds());
        componentDao.deleteById(id);
    }

    public List<Component> getByNameLike(String nameLike) {
        return componentDao.findAllByNameIsContainingIgnoreCase(nameLike);
    }
}
