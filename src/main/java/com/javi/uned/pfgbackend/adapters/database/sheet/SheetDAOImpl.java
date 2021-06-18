package com.javi.uned.pfgbackend.adapters.database.sheet;

import com.javi.uned.pfgbackend.domain.ports.database.SheetDAO;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SheetDAOImpl implements SheetDAO {

    @Autowired
    private SheetRepository sheetRepository;

    @Override
    public List<Sheet> findAll() {

        List<SheetEntity> sheetEntities = sheetRepository.findAll();

        return sheetEntities.stream()
                .map(SheetEntityTransformer::toDomainObject)
                .collect(Collectors.toList());
    }
}
