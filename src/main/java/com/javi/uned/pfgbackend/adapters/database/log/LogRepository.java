package com.javi.uned.pfgbackend.adapters.database.log;

import com.javi.uned.pfgbackend.adapters.database.log.Log;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends PagingAndSortingRepository<Log, Long> {

}
