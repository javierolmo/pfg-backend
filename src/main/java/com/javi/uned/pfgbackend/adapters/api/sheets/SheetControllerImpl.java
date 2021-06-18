package com.javi.uned.pfgbackend.adapters.api.sheets;

import com.javi.uned.pfgbackend.domain.enums.Formats;
import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.exceptions.RetryException;
import com.javi.uned.pfgbackend.domain.sheet.SheetService;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import io.swagger.annotations.Api;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Api(tags = "Sheets")
public class SheetControllerImpl implements SheetController {

    @Autowired
    private SheetService sheetService;


    public Page<SheetDTO> getSheets(int page, int size, String text) {

        // Request page
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Sheet> sheetPage = sheetService.getSheetPage(pageRequest, text);

        // Transform to DTO and return
        Page<SheetDTO> sheetDTOPage = sheetPage.map(sheet -> SheetDTOTransformer.toTransferObject(sheet, sheetService));
        return sheetDTOPage;
    }

    public List<SheetDTO> getSheets(String nameContains, Boolean finished, Long ownerId, Integer id) {

        // Querying by parameters
        List<Sheet> sheets = sheetService.findBy(id, nameContains, ownerId, finished);

        // Building DTOs and return
        List<SheetDTO> result = sheets.stream()
                .map(sheet1 -> SheetDTOTransformer.toTransferObject(sheet1, sheetService))
                .collect(Collectors.toList());
        return result;
    }

    public Sheet createSheet(SheetDTO sheetDTO) {
        Sheet sheet = new Sheet(
                sheetDTO.getId(),
                sheetDTO.getName(),
                sheetDTO.getDate(),
                sheetDTO.getOwnerId(),
                sheetDTO.getFinished()
        );
        return sheetService.save(sheet);
    }

    public ResponseEntity sheet(Integer id) {

        try {
            Sheet sheet = sheetService.getSheet(id);

            SheetDTO sheetDTO = SheetDTOTransformer.toTransferObject(sheet, sheetService);
            return ResponseEntity.ok(sheetDTO);
        } catch (EntityNotFound entityNotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entityNotFound.getMessage());
        }
    }

    public ResponseEntity<String> deleteSheet(int id) {
        sheetService.delete(id);
        return ResponseEntity.ok("Partitura eliminada con éxito");
    }

    public ResponseEntity visualizePDF(int id) {

        try {
            // File
            File file = sheetService.pdfFile(id);
            byte[] bytes = FileUtils.readFileToByteArray(file);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.APPLICATION_PDF); Creo que no haría falta esto
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trying to read pdf");
        }
    }

    public ResponseEntity visualizeXML(int id) {

        try {
            // File
            File file = sheetService.xmlFile(id);
            byte[] bytes = FileUtils.readFileToByteArray(file);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.APPLICATION_XML); Creo que no haría falta esto
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trying to read musicxml");
        }


    }

    public ResponseEntity visualizeSpecs(int id) {
        try {
            // File
            File file = sheetService.specsFile(id);
            byte[] bytes = FileUtils.readFileToByteArray(file);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            // headers.setContentType(MediaType.APPLICATION_JSON); Creo que no haría falta
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error trying to read specs");
        }
    }

    public ResponseEntity<String> retry(int id) {
        try {
            sheetService.retry(id);
            return ResponseEntity.ok("Retry scheduled successfuly");
        } catch (RetryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity downloadFileXML(int id) {
        try {
            Sheet sheet = sheetService.getSheet(id);
            File file = sheetService.xmlFile(id);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sheet.getName() + Formats.MUSICXML + "\"")
                    .contentLength(file.length())
                    //.contentType(MediaType.APPLICATION_OCTET_STREAM) creo que ya no hace falta, lo puse por anotación en la interfaz...
                    .body(resource);
        } catch (EntityNotFound enf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(enf.getMessage());
        } catch (FileNotFoundException fnf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find file in system storage");
        }
    }

    public ResponseEntity downloadFilePDF(int id) {
        try {
            Sheet sheet = sheetService.getSheet(id);
            File file = sheetService.pdfFile(id);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sheet.getName() + Formats.PDF + "\"")
                    .contentLength(file.length())
                    //.contentType(MediaType.APPLICATION_OCTET_STREAM) creo que ya no hace falta, lo puse por anotación en la interfaz...
                    .body(resource);
        } catch (EntityNotFound enf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(enf.getMessage());
        } catch (FileNotFoundException fnf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find file in system storage");
        }
    }
}
