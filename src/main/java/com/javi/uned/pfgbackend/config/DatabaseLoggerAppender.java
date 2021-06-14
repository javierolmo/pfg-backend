package com.javi.uned.pfgbackend.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.javi.uned.pfgbackend.adapters.database.log.LogEntity;
import com.javi.uned.pfgbackend.adapters.database.log.LogRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DatabaseLoggerAppender extends AppenderBase<ILoggingEvent> {

    private static LogRepository recordRepository;

    @Override
    protected void append(ILoggingEvent logEvent) {

        if (!logEvent.getLoggerName().matches("com.javi.uned.pfgbackend.adapters.api.*")) return;

        LogEntity logEntity = new LogEntity();

        Instant instant = Instant.ofEpochMilli(logEvent.getTimeStamp());
        logEntity.setDate(instant.atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss.SS dd/MM/yyyy")));
        logEntity.setMessage(logEvent.getFormattedMessage());
        logEntity.setStatus(logEvent.getLevel().levelStr.toUpperCase(Locale.ROOT));
        logEntity.setAction("");

        recordRepository.save(logEntity);
    }

    public static void setRecordRepository(LogRepository recordRepository) {
        DatabaseLoggerAppender.recordRepository = recordRepository;
    }
}
