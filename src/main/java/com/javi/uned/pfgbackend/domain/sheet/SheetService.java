package com.javi.uned.pfgbackend.domain.sheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javi.uned.pfg.model.Specs;
import com.javi.uned.pfgbackend.adapters.database.sheet.SheetEntity;
import com.javi.uned.pfgbackend.adapters.database.sheet.SheetRepository;
import com.javi.uned.pfgbackend.adapters.filesystem.FileService;
import com.javi.uned.pfgbackend.domain.enums.Formats;
import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.exceptions.RetryException;
import com.javi.uned.pfgbackend.domain.exceptions.UnavailableResourceException;
import com.javi.uned.pfgbackend.domain.sheet.model.Availability;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SheetService {

    private static final Logger logger = LoggerFactory.getLogger(SheetService.class);

    @Autowired
    private SheetRepository sheetRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private KafkaTemplate<String, Specs> retryXmlTemplate;
    @Autowired
    private KafkaTemplate<String, byte[]> retryPdfTemplate;

    public Sheet save(Sheet sheet) {
        SheetEntity sheetEntity = sheet.toEntity();
        sheetEntity = sheetRepository.save(sheetEntity);
        return sheetEntity.toSheet();
    }

    public Sheet getSheet(int id) throws EntityNotFound {
        Optional<SheetEntity> optionalSheetEntity = sheetRepository.findById(id);
        if (!optionalSheetEntity.isPresent()) {
            throw new EntityNotFound("Could not find sheet with id '" + id + "' in database.");
        } else {
            SheetEntity sheetEntity = optionalSheetEntity.get();
            return sheetEntity.toSheet();
        }
    }

    public void delete(int id) {
        sheetRepository.deleteById(id);
        fileService.deleteSheetFolder(id);
    }

    public void retry(int id) throws RetryException {

        try {
            File specsFile = new File(fileService.getSheetFolder(id), "specs.json");
            Optional<SheetEntity> optionalSheet = sheetRepository.findById(id);

            if (specsFile.exists() && optionalSheet.isPresent()) {
                File xml = new File(fileService.getSheetFolder(id), id + Formats.MUSICXML);
                File pdf = new File(fileService.getSheetFolder(id), id + Formats.PDF);
                SheetEntity sheet = optionalSheet.get();
                if (!xml.exists()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Specs specs = objectMapper.readValue(specsFile, Specs.class);
                    String sheetid = "" + id;
                    retryXmlTemplate.send("melodia.backend.retryxml", sheetid, specs);
                    sheet.setFinished(false);
                    sheetRepository.save(sheet);
                } else if (!pdf.exists()) {
                    byte[] xmlbinary = FileUtils.readFileToByteArray(xml);
                    String sheetid = "" + id;
                    retryPdfTemplate.send("melodia.backend.retrypdf", sheetid, xmlbinary);
                    sheet.setFinished(false);
                    sheetRepository.save(sheet);
                } else {
                    throw new RetryException("Partitura correcta. Se ha omitido el reintento");
                }
            } else {
                throw new RetryException("Imposible reconstruir. No se han encontrado las especificaciones. Eliminar partitura o contactar con administrador");
            }

        } catch (IOException ioe) {
            throw new RetryException("Error retrying because of IO error", ioe);
        }
    }

    public Page<Sheet> getSheetPage(PageRequest pageRequest, String name) {

        // Build example
        SheetEntity sheetEntity = new SheetEntity();
        sheetEntity.setName(name);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<SheetEntity> sheetEntityExample = Example.of(sheetEntity, exampleMatcher);

        // Transform result and return
        Page<SheetEntity> sheetEntityPage = sheetRepository.findAll(sheetEntityExample, pageRequest);
        Page<Sheet> sheetPage = sheetEntityPage.map(sheetEntity1 -> sheetEntity1.toSheet());

        return sheetPage;

    }

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
                .map(sheetEntity1 -> sheetEntity1.toSheet())
                .collect(Collectors.toList());
    }

    public void markAsFinished(int id) {

        try {
            // Check files exist
            Availability availability = getAvailability(id);
            availability.checkAvailability();

            Optional<SheetEntity> optionalSheetEntity = sheetRepository.findById(id);
            if(optionalSheetEntity.isPresent()) {
                SheetEntity sheetEntity = optionalSheetEntity.get();
                sheetEntity.setFinished(true);
                sheetRepository.save(sheetEntity);
            } else {
                throw new EntityNotFound("Could not find sheet with id '" + id + "'");
            }
        } catch (EntityNotFound entityNotFound) {
            logger.warn("Could not find entity trying to mark sheet as finished: ", entityNotFound);
        } catch (UnavailableResourceException e) {
            logger.warn("Could not find resource trying to mark sheet as finished: ", e);
        }

    }

    public Availability getAvailability(long id) {
        boolean specs = fileService.hasSpecs(id);
        boolean xml = fileService.hasXML(id);
        boolean pdf = fileService.hasPDF(id);
        return new Availability(specs, xml, pdf);
    }

    public File pdfFile(long id) throws FileNotFoundException {
        File file = new File(fileService.getSheetFolder(id), id + Formats.PDF);

        if (!file.exists()) {
            throw new FileNotFoundException("PDF file not found");
        }

        return file;
    }

    public File xmlFile(long id) throws FileNotFoundException {
        File file = new File(fileService.getSheetFolder(id), id + Formats.MUSICXML);

        if (!file.exists()) {
            throw new FileNotFoundException("PDF file not found");
        }

        return file;
    }

    public File specsFile(long id) throws FileNotFoundException {
        File file = new File(fileService.getSheetFolder(id), "specs.json");

        if (!file.exists()) {
            throw new FileNotFoundException("PDF file not found");
        }

        return file;
    }
}
