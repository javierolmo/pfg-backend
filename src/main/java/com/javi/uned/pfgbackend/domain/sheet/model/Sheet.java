package com.javi.uned.pfgbackend.domain.sheet.model;

public class Sheet {

    private final Integer id;
    private final String name;
    private final String date;
    private final Long ownerId;
    private final Boolean finished;

    public Sheet(Integer id, String name, String date, Long ownerId, Boolean finished) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.ownerId = ownerId;
        this.finished = finished;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Boolean getFinished() {
        return finished;
    }

}
