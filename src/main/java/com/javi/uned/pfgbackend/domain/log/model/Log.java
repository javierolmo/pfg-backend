package com.javi.uned.pfgbackend.domain.log.model;

public class Log {

    private final Long id;
    private final String date;
    private final String status;
    private final String action;
    private final String message;

    public Log(Long id, String date, String status, String action, String message) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.action = action;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
