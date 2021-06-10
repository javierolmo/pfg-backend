package com.javi.uned.pfgbackend.adapters.messagebroker;

import com.javi.uned.pfgbackend.domain.exceptions.EntityNotFound;
import com.javi.uned.pfgbackend.domain.sheet.SheetService;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import com.javi.uned.pfgbackend.adapters.database.sheet.SheetRepository;
import com.javi.uned.pfgbackend.config.FileSystemConfig;
import com.javi.uned.pfgbackend.domain.enums.Formats;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class XmlConsumer {

    private Logger logger = LoggerFactory.getLogger(XmlConsumer.class);

    @Autowired
    private FileSystemConfig fileSystemConfig;
    @Autowired
    private SheetService sheetService;

    @KafkaListener(topics = "melodia.composer.xml", groupId = "0", containerFactory = "fileListenerFactory")
    public void consumeXML(byte[] rawFile, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) throws IOException {

        long keyLong = Long.parseLong(key);

        // Receive and save xml
        File dir = fileSystemConfig.getSheetFolder(keyLong);
        File musicxmlFile = new File(String.format("%s/%s%s", dir.getAbsolutePath(), key, Formats.MUSICXML));
        FileUtils.writeByteArrayToFile(musicxmlFile, rawFile);

        // Mark as finished
        if (fileSystemConfig.hasPDF(keyLong) && fileSystemConfig.hasXML(keyLong)) {
            try {
                int id = Integer.parseInt(key);
                sheetService.markAsFinished(id);
            } catch (EntityNotFound enfe) {
                logger.error("Could not mark sheet as finished: " + enfe.getMessage());
            }
        }
    }
}
