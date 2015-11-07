package com.rocyuan.zero.rest;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rocyuan.zero.domain.Task;
import com.rocyuan.zero.service.TaskService;
import com.rocyuan.zero.web.MediaTypes;

@RestController
@RequestMapping(value = "/task")
public class TaskRestController {
	private static Logger logger = LoggerFactory.getLogger(TaskRestController.class);

	@Autowired
	private TaskService taskService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<Task> list() {
		return taskService.getAllTask();
	}


	@RequestMapping("/qrcode")
	public String getQrcode(){
		return "http://weixin.qq.com/g/A4zsXaIquTh_7N36";
	}



}
