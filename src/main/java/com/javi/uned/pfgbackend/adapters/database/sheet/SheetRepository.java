package com.javi.uned.pfgbackend.adapters.database.sheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends JpaRepository<SheetEntity, Integer> {

}
