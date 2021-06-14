package com.javi.uned.pfgbackend.adapters.database.log;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends PagingAndSortingRepository<LogEntity, Long> {

}
