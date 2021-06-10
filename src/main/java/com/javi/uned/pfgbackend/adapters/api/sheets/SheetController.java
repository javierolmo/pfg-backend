package com.javi.uned.pfgbackend.adapters.api.sheets;

import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface SheetController {

    /**
     * TODO
     *
     * @param nameContains
     * @param finished
     * @param ownerId
     * @param id
     * @return
     */
    @GetMapping(value = "/api/sheets", produces = MediaType.APPLICATION_JSON_VALUE)
    List<SheetDTO> getSheets(
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) Boolean finished,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Integer id
    );

    /**
     * TODO
     *
     * @param sheetDTO
     * @return
     */
    @PostMapping(value = "/api/sheets", produces = MediaType.APPLICATION_JSON_VALUE)
    Sheet createSheet(@RequestBody SheetDTO sheetDTO);

    /**
     * TODO:
     *
     * @param page
     * @param size
     * @param text
     * @return
     */
    @GetMapping(value = "/api/sheets/page", produces = MediaType.APPLICATION_JSON_VALUE)
    Page<SheetDTO> getSheets(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(required = false, defaultValue = "") String text
    );

    /**
     * TODO
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/api/sheets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity sheet(@PathVariable Integer id);


    /**
     * Elimina la partitura seleccionada del sistema de archivos y de la base de datos.
     *
     * @param id identificador de la partitura a eliminar
     */
    @DeleteMapping(value = "/api/sheets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteSheet(@PathVariable int id);

    /**
     * Permite visualizar una partitura en pdf
     *
     * @param id identificador de la partitura
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/api/sheets/{id}.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity visualizePDF(@PathVariable int id);

    /**
     * Permite visualizar una partitura en musicxml
     *
     * @param id identificador de la partitura
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/api/sheets/{id}.musicxml", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity visualizeXML(@PathVariable int id);

    /**
     * Permite visualizar una partitura en musicxml
     *
     * @param id identificador de la partitura
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/api/sheets/{id}.json", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity visualizeSpecs(@PathVariable int id);

    /**
     * TODO
     *
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/api/sheets/{id}/retry", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> retry(@PathVariable int id);

    /**
     * TODO
     *
     * @param id
     * @return
     * @throws FileNotFoundException
     */
    @GetMapping(value = "/api/sheets/{id}/file/musicxml")
    ResponseEntity<Resource> downloadFileXML(@PathVariable int id);

    /**
     * TODO
     *
     * @param id
     * @return
     * @throws FileNotFoundException
     */
    @GetMapping(value = "/api/sheets/{id}/file/pdf", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity downloadFilePDF(@PathVariable int id);
}
