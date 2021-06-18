package com.javi.uned.pfgbackend.adapters.api.sheets;

import com.javi.uned.pfgbackend.domain.sheet.SheetService;
import com.javi.uned.pfgbackend.domain.sheet.model.Availability;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;

public class SheetDTOTransformer {

    private SheetDTOTransformer() {

    }

    public static Sheet toDomainObject(SheetDTO sheetDTO) {
        return new Sheet(
                sheetDTO.getId(),
                sheetDTO.getName(),
                sheetDTO.getDate(),
                sheetDTO.getOwnerId(),
                sheetDTO.getFinished()
        );
    }

    public static SheetDTO toTransferObject(Sheet sheet, SheetService sheetService) {
        SheetDTO sheetDTO = new SheetDTO();
        sheetDTO.setId(sheet.getId());
        sheetDTO.setName(sheet.getName());
        sheetDTO.setDate(sheet.getDate());
        sheetDTO.setOwnerId(sheet.getOwnerId());
        sheetDTO.setFinished(sheet.getFinished());

        // Check availability
        Availability availability = sheetService.getAvailability(sheet.getId());
        sheetDTO.setSpecs(availability.isSpecs());
        sheetDTO.setXml(availability.isXml());
        sheetDTO.setPdf(availability.isPdf());

        return sheetDTO;
    }
}
