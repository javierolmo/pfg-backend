package com.javi.uned.pfgbackend.adapters.database.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javi.uned.pfgbackend.adapters.database.privilege.PrivilegeEntity;
import com.javi.uned.pfgbackend.adapters.database.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "roles")
public class RoleEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "roleEntities")
    private Collection<UserEntity> userEntities;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<PrivilegeEntity> privilegeEntities;

    public RoleEntity() {
        userEntities = new ArrayList<>();
    }

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

    public Collection<UserEntity> getUserEntities() {
        return userEntities;
    }

    public Collection<PrivilegeEntity> getPrivilegeEntities() {
        return privilegeEntities;
    }

    public void setPrivilegeEntities(Collection<PrivilegeEntity> privilegeEntities) {
        this.privilegeEntities = privilegeEntities;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }

}
