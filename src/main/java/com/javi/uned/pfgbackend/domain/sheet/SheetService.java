package com.javi.uned.pfgbackend.domain.sheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javi.uned.pfg.model.Specs;
import com.javi.uned.pfgbackend.adapters.filesystem.FileService;
import com.javi.uned.pfgbackend.domain.enums.Formats;
import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.exceptions.RetryException;
import com.javi.uned.pfgbackend.domain.exceptions.UnavailableResourceException;
import com.javi.uned.pfgbackend.domain.ports.database.SheetDAO;
import com.javi.uned.pfgbackend.domain.sheet.model.Availability;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class SheetService {

    private static final Logger logger = LoggerFactory.getLogger(SheetService.class);

    @Autowired
    private SheetDAO sheetDAO;
    @Autowired
    private FileService fileService;
    @Autowired
    private KafkaTemplate<String, Specs> retryXmlTemplate;
    @Autowired
    private KafkaTemplate<String, byte[]> retryPdfTemplate;

    public Sheet save(Sheet sheet) {
        return this.sheetDAO.save(sheet);
    }

    public Sheet getSheet(int id) throws EntityNotFound {
        return this.sheetDAO.findById(id);
    }

    public void delete(int id) {
        sheetDAO.deleteById(id);
        fileService.deleteSheetFolder(id);
    }

    public void retry(int id) throws RetryException {

        try {
            File specsFile = new File(fileService.getSheetFolder(id), "specs.json");
            Sheet sheet = sheetDAO.findById(id);

            if (specsFile.exists()) {
                File xml = new File(fileService.getSheetFolder(id), id + Formats.MUSICXML);
                File pdf = new File(fileService.getSheetFolder(id), id + Formats.PDF);

                if (xml.exists() && pdf.exists()) {
                    throw new RetryException("Omitido el reintento. Ya existen el xml y el pdf");
                }

                if (!xml.exists()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Specs specs = objectMapper.readValue(specsFile, Specs.class);
                    String sheetid = "" + id;
                    retryXmlTemplate.send("melodia.backend.retryxml", sheetid, specs);
                    sheetDAO.markAsFinished(id);
                }

                if (!pdf.exists()) {
                    byte[] xmlbinary = FileUtils.readFileToByteArray(xml);
                    String sheetid = "" + id;
                    retryPdfTemplate.send("melodia.backend.retrypdf", sheetid, xmlbinary);
                    sheetDAO.markAsFinished(id);
                }

            } else {
                throw new RetryException("Imposible reconstruir. No se han encontrado las especificaciones. Eliminar partitura o contactar con administrador");
            }

        } catch (IOException ioe) {
            throw new RetryException("Error retrying because of IO error", ioe);
        } catch (EntityNotFound entityNotFound) {
            //TODO:
        }
    }

    public Page<Sheet> getSheetPage(PageRequest pageRequest, String name) {
        return sheetDAO.getSheetPage(pageRequest, name);
    }

    public List<Sheet> findBy(Integer id, String nameContains, Long ownerId, Boolean finished) {
        return this.sheetDAO.findBy(id, nameContains, ownerId, finished);
    }

    public void markAsFinished(int id) {

        try {
            // Check files exist
            Availability availability = getAvailability(id);
            availability.checkAvailability();
            sheetDAO.markAsFinished(id);
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
