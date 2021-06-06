package com.javi.uned.pfgbackend.adapters.database.sheet;

import com.javi.uned.pfgbackend.adapters.database.sheet.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends JpaRepository<Sheet, Integer> {


    @Query(nativeQuery = true, value = "SELECT * FROM sheets WHERE name REGEXP ?1 OR style REGEXP ?1")
    Page<Sheet> find(String text, PageRequest of);

}
