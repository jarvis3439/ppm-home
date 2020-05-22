package com.jarvis.ppm.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
			@PathVariable String backlog_id, Principal principal) {
		ResponseEntity<?> errorMap = errorValidationService.validationService(result);
		if (errorMap != null) {
			return errorMap;
		}
		ProjectTask projectTaskObject = projectTaskService.addProjectTask(projectTask, backlog_id, principal.getName());

		return new ResponseEntity<ProjectTask>(projectTaskObject, HttpStatus.CREATED);
	}

	@GetMapping("/{backlog_id}")
	public ResponseEntity<List<ProjectTask>> getProjectBacklog(@PathVariable String backlog_id, Principal principal) {

		return new ResponseEntity<List<ProjectTask>>(
				projectTaskService.findByBacklogId(backlog_id, principal.getName()), HttpStatus.OK);
	}

	@GetMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> findProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id,
			Principal principal) {
		ProjectTask projectTask = projectTaskService.findPTbyProjectSequence(backlog_id, pt_id, principal.getName());
		return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
	}

	@PatchMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
			@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
		ResponseEntity<?> errorMap = errorValidationService.validationService(result);
		if (errorMap != null) {
			return errorMap;
		}
		ProjectTask updatedProjectTask = projectTaskService.updateProjectTask(projectTask, backlog_id, pt_id,
				principal.getName());
		return new ResponseEntity<ProjectTask>(updatedProjectTask, HttpStatus.OK);
	}

	@DeleteMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id,
			Principal principal) {
		projectTaskService.deleteProjectTask(backlog_id, pt_id, principal.getName());
		return new ResponseEntity<String>("Project Task '" + pt_id + "' was deleted successfully", HttpStatus.OK);
	}
}
