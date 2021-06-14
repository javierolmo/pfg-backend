package com.javi.uned.pfgbackend.adapters.filesystem;

import com.javi.uned.pfgbackend.domain.enums.Formats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileService {

    public static final String DATA_FOLDER_PATH = "data";
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final File baseDirectory = new File(DATA_FOLDER_PATH);

    public FileService () {
        if(baseDirectory.isDirectory()) {
            logger.info("Utilizando sistema de archivos ya existente: {}", baseDirectory.getAbsolutePath());
        } else {
            baseDirectory.mkdirs();
            logger.warn("No se ha encontrado una estructura de ficheros previa. Se ha inicializado una nueva: {}", baseDirectory.getAbsolutePath());
        }
    }

    public File getSheetFolder(long sheetId) {
        File result = new File(baseDirectory, ""+sheetId);
        if(!result.isDirectory()){
            result.mkdir();
        }
        return result;
    }

    public boolean deleteSheetFolder(long sheetId) {
        File folder = new File(baseDirectory, "" + sheetId);
        if (folder.exists()) {
            return folder.delete();
        } else {
            return true;
        }
    }

    public boolean exists(long sheetId) {
        File folder = new File(baseDirectory, ""+sheetId);
        return folder.exists();
    }

    public boolean hasPDF(long sheetId) {
        File folder = new File(baseDirectory, ""+sheetId);
        File file = new File(folder, sheetId + Formats.PDF);
        return file.exists();
    }

    public boolean hasXML(long sheetId) {
        File folder = new File(baseDirectory, ""+sheetId);
        File file = new File(folder, sheetId + Formats.MUSICXML);
        return file.exists();
    }

    public Boolean hasSpecs(long sheetId) {
        File folder = new File(baseDirectory, ""+sheetId);
        File file = new File(folder, "specs.json");
        return file.exists();
    }

    public File baseDirectory() {
        return baseDirectory;
    }

}
