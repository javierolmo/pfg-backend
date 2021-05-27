package com.javi.uned.pfgweb.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileSystemConfigTest {

    @Autowired
    private FileSystemConfig fileSystemConfig;

    @Test
    void FileSystem_GetNonExistingFolder_CreateFolderAndReturn() {
        long sheetId = 934712381;
        File nonExistingFolder = new File(FileSystemConfig.DATA_FOLDER_PATH + "/" + sheetId);
        assert !nonExistingFolder.isDirectory();
        File createdFolder = fileSystemConfig.getSheetFolder(sheetId);
        assertEquals(nonExistingFolder.getAbsolutePath(), createdFolder.getAbsolutePath());
        assert createdFolder.delete();
    }

    @Test
    void FileSystem_GetExistingFolder_ReturnFolder() {
        long sheetId = 857123856;
        File nonExistingFolder = new File(FileSystemConfig.DATA_FOLDER_PATH + "/" + sheetId);
        assert !nonExistingFolder.isDirectory();
        assert nonExistingFolder.mkdir();
        File createdFolder = fileSystemConfig.getSheetFolder(sheetId);
        assertEquals(nonExistingFolder.getAbsolutePath(), createdFolder.getAbsolutePath());
        assert createdFolder.delete();
    }

    @Test
    void FileSystem_DeleteExistingFolder_FolderDeleted() {
        long sheetId = 37581236;
        File folderToDelete = new File(FileSystemConfig.DATA_FOLDER_PATH + "/" + sheetId);
        assert folderToDelete.mkdir();
        assert fileSystemConfig.deleteSheetFolder(sheetId);
        assert !folderToDelete.exists();
    }

    @Test
    void FileSystem_DeleteNonExistingFolder_NotDeleteButReturnTrue() {
        long sheetId = 571238561;
        File folderToDelete = new File(FileSystemConfig.DATA_FOLDER_PATH + "/" + sheetId);
        assert !folderToDelete.exists();
        assert fileSystemConfig.deleteSheetFolder(sheetId);
        assert !folderToDelete.exists();
    }

}