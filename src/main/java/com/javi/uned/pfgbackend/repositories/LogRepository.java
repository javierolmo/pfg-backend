package com.javi.uned.pfgbackend.repositories;

import com.javi.uned.pfgbackend.beans.Log;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends PagingAndSortingRepository<Log, Long> {

}
