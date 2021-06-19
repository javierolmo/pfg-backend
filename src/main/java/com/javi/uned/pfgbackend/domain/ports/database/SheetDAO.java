package com.javi.uned.pfgbackend.domain.ports.database;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SheetDAO {

    List<Sheet> findAll();

    Sheet save(Sheet sheet);

    Sheet findById(int id) throws EntityNotFound;

    void deleteById(int id);

    Sheet markAsFinished(int id) throws EntityNotFound;

    List<Sheet> findBy(Integer id, String nameContains, Long ownerId, Boolean finished);

    Page<Sheet> getSheetPage(PageRequest pageRequest, String name);
}
