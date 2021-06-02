package com.javi.uned.pfgbackend.services;

import com.javi.uned.pfgbackend.beans.Sheet;
import com.javi.uned.pfgbackend.config.FileSystemConfig;
import com.javi.uned.pfgbackend.util.Formats;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class ComposerResultConsumer {

    @Autowired
    private FileSystemConfig fileSystemConfig;

    @KafkaListener(topics = "melodia.composer.xml", groupId = "0", containerFactory = "fileListenerFactory")
    public void consumeXML(byte[] rawFile, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) throws IOException {
        File dir = fileSystemConfig.getSheetFolder(Long.parseLong(key));
        File musicxmlFile = new File(String.format("%s/%s%s", dir.getAbsolutePath(), key, Formats.MUSICXML));
        FileUtils.writeByteArrayToFile(musicxmlFile, rawFile);
    }

    @KafkaListener(topics = "melodia.composer.pdf", groupId = "0", containerFactory = "fileListenerFactory")
    public void consumePDF(byte[] rawFile, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) throws IOException {
        File dir = fileSystemConfig.getSheetFolder(Long.parseLong(key));
        File pdfFile = new File(String.format("%s/%s%s", dir.getAbsolutePath(), key, Formats.PDF));
        FileUtils.writeByteArrayToFile(pdfFile, rawFile);
    }


}
