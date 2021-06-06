package com.javi.uned.pfgbackend.adapters.database.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query("from RoleEntity r where r.name = ?1")
    RoleEntity findByName(String name);
}
