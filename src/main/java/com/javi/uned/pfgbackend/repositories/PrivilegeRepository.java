package com.javi.uned.pfgbackend.repositories;

import com.javi.uned.pfgbackend.beans.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    @Query("from Privilege p where p.name = ?1")
    Privilege findByName(String name);
}
