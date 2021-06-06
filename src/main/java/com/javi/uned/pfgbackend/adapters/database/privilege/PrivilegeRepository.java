package com.javi.uned.pfgbackend.adapters.database.privilege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {

    @Query("from PrivilegeEntity p where p.name = ?1")
    PrivilegeEntity findByName(String name);
}
