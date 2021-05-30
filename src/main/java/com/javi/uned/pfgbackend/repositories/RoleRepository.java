package com.javi.uned.pfgbackend.repositories;

import com.javi.uned.pfgbackend.beans.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("from Role r where r.name = ?1")
    Role findByName(String name);
}
