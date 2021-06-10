package com.javi.uned.pfgbackend.adapters.api.sheets;

import com.javi.uned.pfgbackend.config.FileSystemConfig;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;

public class SheetDTOTransformer {

    public static Sheet toDomainObject(SheetDTO sheetDTO) {
        return new Sheet(sheetDTO.getId(), sheetDTO.getName(), sheetDTO.getDate(), sheetDTO.getOwnerId(), sheetDTO.getFinished());
    }

    public static SheetDTO toTransferObject(Sheet sheet, FileSystemConfig fileSystemConfig) {
        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setId(sheet.getId());
        sheetDTO.setName(sheet.getName());
        sheetDTO.setDate(sheet.getDate());
        sheetDTO.setOwnerId(sheet.getOwnerId());
        sheetDTO.setFinished(sheet.getFinished());
        sheetDTO.setSpecs(fileSystemConfig.hasSpecs(sheet.getId())); //TODO: Esto debería hacerlo a través del sheetService, desde el api no debería tener conocimiento de nada más que no sea el dominio
        sheetDTO.setXml(fileSystemConfig.hasXML(sheet.getId()));
        sheetDTO.setPdf(fileSystemConfig.hasPDF(sheet.getId()));

        return sheetDTO;
    }
}
