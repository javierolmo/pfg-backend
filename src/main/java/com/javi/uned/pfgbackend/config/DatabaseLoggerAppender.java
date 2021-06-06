package com.javi.uned.pfgbackend.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.javi.uned.pfgbackend.adapters.database.log.Log;
import com.javi.uned.pfgbackend.adapters.database.log.LogRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DatabaseLoggerAppender extends AppenderBase<ILoggingEvent> {

    private static LogRepository recordRepository;

    @Override
    protected void append(ILoggingEvent logEvent) {

        if (!logEvent.getLoggerName().matches("com.javi.uned.*")) return;

        Log log = new Log();

        Instant instant = Instant.ofEpochMilli(logEvent.getTimeStamp());
        log.setDate(instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss.SS dd/MM/yyyy")));
        log.setMessage(logEvent.getFormattedMessage());
        log.setStatus(logEvent.getLevel().levelStr.toUpperCase(Locale.ROOT));
        log.setAction("");

        recordRepository.save(log);
    }

    public static void setRecordRepository(LogRepository recordRepository) {
        DatabaseLoggerAppender.recordRepository = recordRepository;
    }
}
