package com.jarvis.ppm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/{projectIdentifer}")
	public ResponseEntity<?> getProjectByIdentifer(@PathVariable String projectIdentifer) {
		Project project = projectService.findProjectByIdentifier(projectIdentifer);
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	@GetMapping("/all")
	public List<Project> getAllProjects() {
		return projectService.findAllProjects();
	}

	@DeleteMapping("/{projectIdentifier}")
	public ResponseEntity<?> deleteProject(@PathVariable String projectIdentifier) {
		projectService.deleteProjectByIdentifier(projectIdentifier);
		return new ResponseEntity<String>("Project with Identifier '" + projectIdentifier.toUpperCase() + "' was deleted", HttpStatus.OK);
	}
}
