package com.javi.uned.pfgbackend.adapters.api.sheets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javi.uned.pfg.model.Specs;
import com.javi.uned.pfgbackend.adapters.database.sheet.Sheet;
import com.javi.uned.pfgbackend.adapters.database.sheet.SheetRepository;
import com.javi.uned.pfgbackend.config.FileSystemConfig;
import com.javi.uned.pfgbackend.domain.enums.Formats;
import io.swagger.annotations.Api;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@Api(tags = "Sheets")
public class SheetControllerImpl implements SheetController{

    @Autowired
    private SheetRepository sheetRepository;
    @Autowired
    private FileSystemConfig fileSystemConfig;
    @Autowired
    private KafkaTemplate<String, Specs> retryXmlTemplate;
    @Autowired
    private KafkaTemplate<String, byte[]> retryPdfTemplate;

    public Page<Sheet> getSheets(int page, int size, String text) {
        text = ".*" + text + ".*";
        return sheetRepository.find(text, PageRequest.of(page, size));
    }

    public List<SheetDTO> getSheets(String nameContains, Boolean finished, Long ownerId, Integer id) {

        // Querying by example
        Sheet sheet = new Sheet();
        sheet.setFinished(finished);
        sheet.setName(nameContains);
        sheet.setOwnerId(ownerId);
        sheet.setId(id);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", GenericPropertyMatcher::contains);
        Example<Sheet> sheetExample = Example.of(sheet, matcher);
        List<Sheet> sheets = sheetRepository.findAll(sheetExample);

        // Building DTOs
        List<SheetDTO> result = new ArrayList<>();
        sheets.forEach(sheet1 -> {
            SheetDTO sheetDTO = new SheetDTO();
            sheetDTO.setFinished(sheet1.getFinished());
            sheetDTO.setName(sheet1.getName());
            sheetDTO.setOwnerId(sheet1.getOwnerId());
            sheetDTO.setId(sheet1.getId());
            sheetDTO.setStyle(sheet1.getStyle());
            sheetDTO.setDate(sheet1.getDate());
            sheetDTO.setSpecs(new File(fileSystemConfig.getSheetFolder(sheet1.getId()), "specs.json").exists());
            sheetDTO.setXml(new File(fileSystemConfig.getSheetFolder(sheet1.getId()), sheet1.getId() + Formats.MUSICXML).exists());
            sheetDTO.setPdf(new File(fileSystemConfig.getSheetFolder(sheet1.getId()), sheet1.getId() + Formats.PDF).exists());
            result.add(sheetDTO);
        });
        return result;
    }

    public Sheet createSheet(SheetDTO sheetDTO) {
        Sheet sheet = new Sheet();
        sheet.setDate(sheetDTO.getDate());
        sheet.setId(sheetDTO.getId());
        sheet.setStyle(sheetDTO.getStyle());
        sheet.setName(sheetDTO.getName());
        sheet.setOwnerId(sheetDTO.getOwnerId());
        sheet.setFinished(sheetDTO.getFinished());
        return sheetRepository.save(sheet);
    }

    public SheetDTO sheet(Integer id) {
        Optional<Sheet> optionalSheet = sheetRepository.findById(id);
        if (optionalSheet.isPresent()) {
            Sheet sheet = optionalSheet.get();
            SheetDTO sheetDTO = new SheetDTO();
            sheetDTO.setFinished(sheet.getFinished());
            sheetDTO.setName(sheet.getName());
            sheetDTO.setOwnerId(sheet.getOwnerId());
            sheetDTO.setId(sheet.getId());
            sheetDTO.setStyle(sheet.getStyle());
            sheetDTO.setDate(sheet.getDate());
            sheetDTO.setSpecs(new File(fileSystemConfig.getSheetFolder(sheet.getId()), "specs.json").exists());
            sheetDTO.setXml(new File(fileSystemConfig.getSheetFolder(sheet.getId()), id + Formats.MUSICXML).exists());
            sheetDTO.setPdf(new File(fileSystemConfig.getSheetFolder(sheet.getId()), id + Formats.PDF).exists());
            return sheetDTO;
        } else {
            return null;
        }
    }

    public ResponseEntity<Sheet> setFinished(Integer id, SheetDTO sheetDTO) {
        Optional<Sheet> optionalSheet = sheetRepository.findById(id);
        if (optionalSheet.isPresent()) {
            Sheet result = optionalSheet.get();
            if (sheetDTO.getFinished() != null) result.setFinished(sheetDTO.getFinished());
            if (sheetDTO.getName() != null) result.setName(sheetDTO.getName());
            if (sheetDTO.getId() != null) result.setId(sheetDTO.getId());
            if (sheetDTO.getDate() != null) result.setDate(sheetDTO.getDate());
            if (sheetDTO.getStyle() != null) result.setStyle(sheetDTO.getStyle());
            if (sheetDTO.getOwnerId() != null) result.setOwnerId(sheetDTO.getOwnerId());
            sheetRepository.save(result);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public String deleteSheet(int id) {
        sheetRepository.deleteById(id);
        fileSystemConfig.deleteSheetFolder(id);
        return "Partitura eliminada con éxito";
    }

    public ResponseEntity<byte[]> visualizePDF(int id) throws IOException {

        // File
        File file = new File(fileSystemConfig.getSheetFolder(id), id + Formats.PDF);
        byte[] bytes = FileUtils.readFileToByteArray(file);

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

    }

    public ResponseEntity<byte[]> visualizeXML(int id) throws IOException {

        // File
        File file = new File(fileSystemConfig.getSheetFolder(id), id + Formats.MUSICXML);
        byte[] bytes = FileUtils.readFileToByteArray(file);

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

    }

    public ResponseEntity<byte[]> visualizeSpecs(int id) throws IOException {

        // File
        File file = new File(fileSystemConfig.getSheetFolder(id), "specs.json");
        byte[] bytes = FileUtils.readFileToByteArray(file);

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

    }

    public ResponseEntity<String> retry(int id) throws IOException {
        File specsFile = new File(fileSystemConfig.getSheetFolder(id), "specs.json");
        Optional<Sheet> optionalSheet = sheetRepository.findById(id);

        if (specsFile.exists() && optionalSheet.isPresent()) {
            File xml = new File(fileSystemConfig.getSheetFolder(id), id + Formats.MUSICXML);
            File pdf = new File(fileSystemConfig.getSheetFolder(id), id + Formats.PDF);
            Sheet sheet = optionalSheet.get();
            if (!xml.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                Specs specs = objectMapper.readValue(specsFile, Specs.class);
                String sheetid = "" + id;
                retryXmlTemplate.send("melodia.backend.retryxml", sheetid, specs);
                sheet.setFinished(false);
                sheetRepository.save(sheet);
                return ResponseEntity.ok("Se ha planificado el reintento. Reconstruyendo xml y pdf...");
            } else if (!pdf.exists()) {
                byte[] xmlbinary = FileUtils.readFileToByteArray(xml);
                String sheetid = "" + id;
                retryPdfTemplate.send("melodia.backend.retrypdf", sheetid, xmlbinary);
                sheet.setFinished(false);
                sheetRepository.save(sheet);
                return ResponseEntity.ok("Se ha planificado el reintento. Reconstruyendo pdf...");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Partitura correcta. Se ha omitido el reintento");
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Imposible reconstruir. No se han encontrado las " +
                    "especificaciones. Eliminar partitura o contactar con administrador");
        }
    }

    public Sheet uploadFileXML(MultipartFile file, Integer id) throws IOException {
        File dir = fileSystemConfig.getSheetFolder(id);
        File musicxmlFile = new File(String.format("%s/%d%s", dir.getAbsolutePath(), id, Formats.MUSICXML));
        Files.copy(file.getInputStream(), musicxmlFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Optional<Sheet> optionalSheet = sheetRepository.findById(id);
        return optionalSheet.isPresent() ? optionalSheet.get() : null;
    }

    public ResponseEntity<Sheet> uploadFilePDF(MultipartFile file, Integer id) throws IOException {
        Optional<Sheet> optionalSheet = sheetRepository.findById(id);
        if (optionalSheet.isPresent()) {
            File dir = fileSystemConfig.getSheetFolder(id);
            File localFile = new File(String.format("%s/%d%s", dir.getAbsolutePath(), id, Formats.PDF));
            Files.copy(file.getInputStream(), localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Sheet sheet = optionalSheet.get();
            sheet.setFinished(true);
            sheetRepository.save(sheet);
            return ResponseEntity.ok(sheet);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<Resource> downloadFileXML(int id) throws FileNotFoundException {
        Optional<Sheet> optionalSheet = sheetRepository.findById(id);
        if (optionalSheet.isPresent()) {
            Sheet sheet = optionalSheet.get();
            File file = new File(fileSystemConfig.getSheetFolder(id), id + Formats.MUSICXML);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sheet.getName() + Formats.MUSICXML + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Resource> downloadFilePDF(int id) throws FileNotFoundException {
        Optional<Sheet> optionalSheet = sheetRepository.findById(id);
        if (optionalSheet.isPresent()) {
            Sheet sheet = optionalSheet.get();
            File file = new File(fileSystemConfig.getSheetFolder(id), id + Formats.PDF);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sheet.getName() + Formats.PDF + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
