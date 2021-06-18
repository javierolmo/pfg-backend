package com.javi.uned.pfgbackend.config;

import com.javi.uned.pfgbackend.adapters.filesystem.FileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileSystemConfig {

    @Bean
    public FileService fileService() {
        return new FileService();
    }

}
