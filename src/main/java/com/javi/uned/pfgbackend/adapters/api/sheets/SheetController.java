package com.javi.uned.pfgbackend.adapters.api.sheets;

import com.javi.uned.pfgbackend.adapters.database.sheet.Sheet;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping(value = "/api/sheets/pages", produces = MediaType.APPLICATION_JSON_VALUE)
    Page<Sheet> getSheets(
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
    SheetDTO sheet(@PathVariable Integer id);

    /**
     * TODO
     *
     * @param id
     * @param sheetDTO
     * @return
     */
    @PutMapping(value = "/api/sheets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Sheet> setFinished(@PathVariable Integer id, @RequestBody SheetDTO sheetDTO);

    /**
     * Elimina la partitura seleccionada del sistema de archivos y de la base de datos.
     *
     * @param id identificador de la partitura a eliminar
     */
    @DeleteMapping(value = "/api/sheets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    String deleteSheet(@PathVariable int id);

    /**
     * Permite visualizar una partitura en pdf
     *
     * @param id identificador de la partitura
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/api/sheets/{id}.pdf")
    ResponseEntity<byte[]> visualizePDF(@PathVariable int id) throws IOException;

    /**
     * Permite visualizar una partitura en musicxml
     *
     * @param id identificador de la partitura
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/api/sheets/{id}.musicxml")
    ResponseEntity<byte[]> visualizeXML(@PathVariable int id) throws IOException;

    /**
     * Permite visualizar una partitura en musicxml
     *
     * @param id identificador de la partitura
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/api/sheets/{id}.json")
    ResponseEntity<byte[]> visualizeSpecs(@PathVariable int id) throws IOException;

    /**
     * TODO
     *
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/api/sheets/{id}/retry", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> retry(@PathVariable int id) throws IOException;

    /**
     * Guarda una partitura en la carpeta 'sheets', renombrando el archivo y modificando la ruta y el estado en la base
     * de datos.
     *
     * @param file partitura en formato .musicxml
     * @param id   identificador de la partitura en base de datos
     * @return devuelve un objeto {@link Sheet} con la información de la partitura
     * @throws IOException
     */
    @PostMapping(value = "/api/sheets/{id}/file/musicxml", produces = MediaType.APPLICATION_JSON_VALUE)
    Sheet uploadFileXML(@RequestBody MultipartFile file, @PathVariable Integer id) throws IOException;

    /**
     * Guarda una partitura en la carpeta 'sheets', renombrando el archivo y modificando la ruta y el estado en la base
     * de datos.
     *
     * @param file partitura en formato .pdf
     * @param id   identificador de la partitura en base de datos
     * @return devuelve un objeto {@link Sheet} con la información de la partitura
     * @throws IOException
     */
    @PostMapping(value = "/api/sheets/{id}/file/pdf")
    ResponseEntity<Sheet> uploadFilePDF(@RequestBody MultipartFile file, @PathVariable Integer id) throws IOException;

    /**
     * TODO
     *
     * @param id
     * @return
     * @throws FileNotFoundException
     */
    @GetMapping(value = "/api/sheets/{id}/file/musicxml")
    ResponseEntity<Resource> downloadFileXML(@PathVariable int id) throws FileNotFoundException;

    /**
     * TODO
     *
     * @param id
     * @return
     * @throws FileNotFoundException
     */
    @GetMapping(value = "/api/sheets/{id}/file/pdf")
    ResponseEntity<Resource> downloadFilePDF(@PathVariable int id) throws FileNotFoundException;
}
