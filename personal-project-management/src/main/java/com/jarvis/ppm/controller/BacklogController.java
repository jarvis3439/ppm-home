package com.jarvis.ppm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jarvis.ppm.model.ProjectTask;
import com.jarvis.ppm.service.ErrorValidationService;
import com.jarvis.ppm.service.ProjectTaskService;

@RestController
@RequestMapping("/backlog")
@CrossOrigin
public class BacklogController {

	@Autowired
	private ProjectTaskService projectTaskService;

	@Autowired
	private ErrorValidationService errorValidationService;

	@PostMapping("/{backlog_id}")
	public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
			@PathVariable String backlog_id) {
		ResponseEntity<?> errorMap = errorValidationService.validationService(result);

		if (errorMap != null) {
			return errorMap;
		}
		ProjectTask projectTaskObject = projectTaskService.addProjectTask(backlog_id, projectTask);

		return new ResponseEntity<ProjectTask>(projectTaskObject, HttpStatus.CREATED);
	}

	@GetMapping("/{backlog_id}")
	public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String backlog_id) {

		return new ResponseEntity<List<ProjectTask>>(projectTaskService.findByBacklogId(backlog_id), HttpStatus.OK);
	}

}
