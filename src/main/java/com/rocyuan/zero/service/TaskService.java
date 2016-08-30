package com.rocyuan.zero.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rocyuan.zero.domain.Task;
import com.rocyuan.zero.repository.TaskDao;


@Component
@Transactional
public class TaskService {

	@Autowired
	private TaskDao taskDao;

	public List<Task> getAllTask() {
		return (List<Task>) taskDao.findAll();
	}
}
