package com.javi.uned.pfgbackend.domain.sheet.model;

import com.javi.uned.pfgbackend.adapters.api.sheets.SheetDTO;
import com.javi.uned.pfgbackend.adapters.api.users.UserDTO;
import com.javi.uned.pfgbackend.adapters.database.sheet.SheetEntity;
import com.javi.uned.pfgbackend.adapters.database.user.UserEntity;

import javax.persistence.*;
import java.util.stream.Collectors;

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

    public SheetDTO toTransferObject() {
        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setId(id);
        sheetDTO.setName(name);
        sheetDTO.setDate(date);
        sheetDTO.setOwnerId(ownerId);
        sheetDTO.setFinished(finished);
        return sheetDTO;
    }

    public SheetEntity toEntity() {
        SheetEntity sheetEntity = new SheetEntity();
        sheetEntity.setId(id);
        sheetEntity.setName(name);
        sheetEntity.setDate(date);
        sheetEntity.setOwnerId(ownerId);
        sheetEntity.setFinished(finished);
        return sheetEntity;
    }
}
