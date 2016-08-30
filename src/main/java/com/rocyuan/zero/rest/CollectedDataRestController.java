package com.rocyuan.zero.rest;

import com.rocyuan.zero.domain.Task;
import com.rocyuan.zero.service.TaskService;
import com.rocyuan.zero.web.MediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/collecteddata")
public class CollectedDataRestController {
	private static Logger logger = LoggerFactory.getLogger(CollectedDataRestController.class);




}
