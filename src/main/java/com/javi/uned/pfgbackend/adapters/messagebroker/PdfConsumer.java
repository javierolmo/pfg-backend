package com.javi.uned.pfgbackend.adapters.messagebroker;

import com.javi.uned.pfgbackend.adapters.database.sheet.Sheet;
import com.javi.uned.pfgbackend.adapters.database.sheet.SheetRepository;
import com.javi.uned.pfgbackend.config.FileSystemConfig;
import com.javi.uned.pfgbackend.domain.enums.Formats;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class PdfConsumer {

    @Autowired
    private FileSystemConfig fileSystemConfig;
    @Autowired
    private SheetRepository sheetRepository;

    @KafkaListener(topics = "melodia.composer.pdf", groupId = "0", containerFactory = "fileListenerFactory")
    public void consumePDF(byte[] rawFile, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) throws IOException {

        long keyLong = Long.parseLong(key);

        // Receive and save pdf
        File dir = fileSystemConfig.getSheetFolder(Long.parseLong(key));
        File pdfFile = new File(String.format("%s/%s%s", dir.getAbsolutePath(), key, Formats.PDF));
        FileUtils.writeByteArrayToFile(pdfFile, rawFile);

        // Mark as finished
        if (fileSystemConfig.hasPDF(keyLong) && fileSystemConfig.hasXML(keyLong)) {
            Optional<Sheet> optionalSheet = sheetRepository.findById(Integer.parseInt(key));
            if (optionalSheet.isPresent()) {
                Sheet sheet = optionalSheet.get();
                sheet.setFinished(true);
                sheetRepository.save(sheet);
            }
        }
    }

}
