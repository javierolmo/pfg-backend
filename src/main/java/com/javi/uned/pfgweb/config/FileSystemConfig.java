package com.javi.uned.pfgweb.config;

import com.javi.uned.pfgweb.repositories.SheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileSystemConfig {

    @Autowired
    private SheetRepository sheetRepository;

    public static final String DATA_FOLDER_PATH = "data";
    private final Logger logger = LoggerFactory.getLogger(FileSystemConfig.class);

    private final File dataFolder = new File(DATA_FOLDER_PATH);

    private FileSystemConfig () {
        if(dataFolder.isDirectory()) {
            logger.info("Utilizando sistema de archivos ya existente: {}", dataFolder.getAbsolutePath());
        } else {
            dataFolder.mkdirs();
            logger.warn("No se ha encontrado una estructura de ficheros previa. Se ha inicializado una nueva: {}", dataFolder.getAbsolutePath());
        }
    }

    public File getSheetFolder(long sheetId) {
        File result = new File(dataFolder, ""+sheetId);
        if(!result.isDirectory()){
            result.mkdir();
        }
        return result;
    }

    public boolean deleteSheetFolder(long sheetId) {
        File folder = new File(dataFolder, "" + sheetId);
        if (folder.exists()) {
            return folder.delete();
        } else {
            return true;
        }
    }

    public boolean exists(long sheetId) {
        File folder = new File(dataFolder, ""+sheetId);
        return folder.exists();
    }

}
