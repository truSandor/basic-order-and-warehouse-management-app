package com.orderandwarehouse.app.service;

import com.orderandwarehouse.app.model.*;
import com.orderandwarehouse.app.repository.ComponentDao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComponentService {
    private final ComponentDao componentDao;

    public List<Component> getAll() {
        return componentDao.findAll();
    }

    public Optional<Component> getById(Long id) {
        return componentDao.findById(id);
    }

    public Component add(@Valid Component component) {
        return componentDao.save(component);
    }

    public Component update(Long id, @Valid Component component) {
        Component componentFromDb = componentDao.findById(id).orElseThrow(NoSuchElementException::new);
        component.setId(componentFromDb.getId());
        component.setPartsListRows(componentFromDb.getPartsListRows());
        component.setStorageUnits(componentFromDb.getStorageUnits());
        //todo check if this saves a new entity/dbLine, or updates the existing one
        return componentDao.save(component);
    }

    public void delete(Long id) {
        componentDao.deleteById(id); //todo: check what happens if we try to delete a Component with Storage-Unit or PartsListRows
        //check commit 9ea0e309 if you want to reroll
    }

    public List<Component> getByNameLike(String nameLike) {
        return componentDao.findAllByNameIsContainingIgnoreCase(nameLike);
    }
}
