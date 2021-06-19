package com.javi.uned.pfgbackend.adapters.database.sheet;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.ports.database.SheetDAO;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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

    @Override
    public Sheet save(Sheet sheet) {
        SheetEntity sheetEntity = SheetEntityTransformer.toEntity(sheet);
        sheetEntity = sheetRepository.save(sheetEntity);

        return SheetEntityTransformer.toDomainObject(sheetEntity);
    }

    @Override
    public Sheet findById(int id) throws EntityNotFound {
        Optional<SheetEntity> optionalSheetEntity = sheetRepository.findById(id);
        if (optionalSheetEntity.isPresent()) {
            return SheetEntityTransformer.toDomainObject(optionalSheetEntity.get());
        } else {
            throw new EntityNotFound("Could not find sheet with id '" + id + "'");
        }
    }

    @Override
    public void deleteById(int id) {
        this.sheetRepository.deleteById(id);
    }

    @Override
    public Sheet markAsFinished(int id) throws EntityNotFound {
        Optional<SheetEntity> optionalSheetEntity = sheetRepository.findById(id);
        if (optionalSheetEntity.isPresent()) {
            SheetEntity sheetEntity = optionalSheetEntity.get();
            sheetEntity.setFinished(true);
            sheetRepository.save(sheetEntity);
            return SheetEntityTransformer.toDomainObject(sheetEntity);
        } else {
            throw new EntityNotFound("Could not find sheet with id '" + id + "'");
        }
    }

    @Override
    public List<Sheet> findBy(Integer id, String nameContains, Long ownerId, Boolean finished) {

        // Create example
        SheetEntity sheetEntity = new SheetEntity();
        sheetEntity.setId(id);
        sheetEntity.setName(nameContains);
        sheetEntity.setOwnerId(ownerId);
        sheetEntity.setFinished(finished);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<SheetEntity> sheetExample = Example.of(sheetEntity, matcher);

        // Query by example
        List<SheetEntity> sheetEntities = sheetRepository.findAll(sheetExample);

        // Transform and return
        return sheetEntities.stream()
                .map(SheetEntityTransformer::toDomainObject)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Sheet> getSheetPage(PageRequest pageRequest, String name) {

        // Build example
        SheetEntity sheetEntity = new SheetEntity();
        sheetEntity.setName(name);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<SheetEntity> sheetEntityExample = Example.of(sheetEntity, exampleMatcher);

        // Transform result and return
        Page<SheetEntity> sheetEntityPage = sheetRepository.findAll(sheetEntityExample, pageRequest);
        return sheetEntityPage.map(SheetEntityTransformer::toDomainObject);
    }
}
