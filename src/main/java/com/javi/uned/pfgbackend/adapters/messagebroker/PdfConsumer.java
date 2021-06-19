package com.javi.uned.pfgbackend.adapters.messagebroker;

import com.javi.uned.pfgbackend.adapters.filesystem.FileService;
import com.javi.uned.pfgbackend.domain.enums.Formats;
import com.javi.uned.pfgbackend.domain.sheet.SheetService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfConsumer {

    @Autowired
    private FileService fileService;
    @Autowired
    private SheetService sheetService;

    @KafkaListener(topics = "melodia.composer.pdf", groupId = "0", containerFactory = "fileListenerFactory")
    public void consumePDF(byte[] rawFile, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) throws IOException {

        long keyLong = Long.parseLong(key);

        // Receive and save pdf
        File dir = fileService.getSheetFolder(Long.parseLong(key));
        File pdfFile = new File(String.format("%s/%s%s", dir.getAbsolutePath(), key, Formats.PDF));
        FileUtils.writeByteArrayToFile(pdfFile, rawFile);

        // Mark as finished
        if (fileService.hasPDF(keyLong) && fileService.hasXML(keyLong)) {
            int id = Integer.parseInt(key);
            sheetService.markAsFinished(id);
        }
    }

}
