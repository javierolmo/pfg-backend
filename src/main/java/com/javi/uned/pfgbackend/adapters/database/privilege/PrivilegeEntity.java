package com.javi.uned.pfgbackend.adapters.database.privilege;

import com.javi.uned.pfgbackend.adapters.database.role.RoleEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "privileges")
public class PrivilegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "privilegeEntities")
    private Collection<RoleEntity> roleEntities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(Collection<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }
}
