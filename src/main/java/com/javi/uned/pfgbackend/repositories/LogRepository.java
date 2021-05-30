package com.javi.uned.pfgbackend.repositories;

import com.javi.uned.pfgbackend.beans.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

}
