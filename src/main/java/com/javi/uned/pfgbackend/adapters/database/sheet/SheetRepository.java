package com.javi.uned.pfgbackend.adapters.database.sheet;

import com.javi.uned.pfgbackend.domain.sheet.model.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends JpaRepository<SheetEntity, Integer> {

}
