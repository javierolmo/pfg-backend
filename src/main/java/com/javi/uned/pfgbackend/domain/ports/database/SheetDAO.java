package com.javi.uned.pfgbackend.domain.ports.database;

import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;

import java.util.List;

public interface SheetDAO {

    List<Sheet> findAll();

}
