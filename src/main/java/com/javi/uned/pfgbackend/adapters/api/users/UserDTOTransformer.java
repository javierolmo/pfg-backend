package com.javi.uned.pfgbackend.adapters.api.users;

import com.javi.uned.pfgbackend.adapters.api.sheets.SheetDTO;
import com.javi.uned.pfgbackend.domain.sheet.SheetService;
import com.javi.uned.pfgbackend.domain.sheet.model.Availability;
import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import com.javi.uned.pfgbackend.domain.user.model.User;

import java.util.ArrayList;

public class UserDTOTransformer {

    public static User toDomainObject(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getName(), userDTO.getSurname(), userDTO.getEnabled(), new ArrayList<>() /*TODO:*/);
    }

    public static UserDTO toTransferObject(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEnabled(user.getEnabled());
        //TODO: Roles
        userDTO.setRoles(new ArrayList<>());

        return userDTO;
    }
}
