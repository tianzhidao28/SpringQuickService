package com.rocyuan.zero.repository;

import org.springframework.data.repository.CrudRepository;

import com.rocyuan.zero.domain.Task;

public interface TaskDao extends CrudRepository<Task, Long> {
	
}
