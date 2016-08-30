package com.rocyuan.zero.repository;

import com.rocyuan.zero.domain.CollectedData;
import org.springframework.data.repository.CrudRepository;

public interface CollectedDataDao extends CrudRepository<CollectedData, Long> {
	
}
