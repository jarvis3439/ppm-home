package com.jarvis.ppm.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jarvis.ppm.model.Project;
import com.jarvis.ppm.service.ErrorValidationService;
import com.jarvis.ppm.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ErrorValidationService errorValidationService;

	@PostMapping("")
	public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {

		ResponseEntity<?> error = errorValidationService.validationService(result);
		if (error != null) {
			return error;
		}

		projectService.saveOrUpdateProject(project);
		System.out.println("Project Created Successfully");
		return new ResponseEntity<Project>(project, HttpStatus.CREATED);
	}
}
