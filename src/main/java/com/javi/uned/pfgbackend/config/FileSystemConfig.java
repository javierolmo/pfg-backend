package com.javi.uned.pfgbackend.config;

import com.javi.uned.pfgbackend.adapters.database.sheet.SheetRepository;
import com.javi.uned.pfgbackend.adapters.filesystem.FileService;
import com.javi.uned.pfgbackend.domain.enums.Formats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

@Configuration
public class FileSystemConfig {

    @Bean
    public FileService fileService() {
        return new FileService();
    }

}
