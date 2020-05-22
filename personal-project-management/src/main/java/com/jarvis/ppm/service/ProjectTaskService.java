package com.jarvis.ppm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.ppm.exception.ProjectNotFoundException;
import com.jarvis.ppm.model.Backlog;
import com.jarvis.ppm.model.Project;
import com.jarvis.ppm.model.ProjectTask;
import com.jarvis.ppm.repository.BacklogRepository;
import com.jarvis.ppm.repository.ProjectRepository;
import com.jarvis.ppm.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	// Add Project Task to particular backlog
	public ProjectTask addProjectTask(ProjectTask projectTask, String projectIdentifier, String username) {

		String identifier = projectIdentifier.toUpperCase();
		// Project Task added to specific project (Project != null)
		Backlog backlog = projectService.findProjectByIdentifier(identifier, username).getBacklog();
		// set the Backlog to the ProjectTask
		projectTask.setBacklog(backlog);
		// Project Sequence
		Integer projectTaskSequence = backlog.getPTSequence();
		// Update PT sequence
		projectTaskSequence++;
		backlog.setPTSequence(projectTaskSequence);
		// add Sequence to Project Task
		projectTask.setProjectSequence(identifier + "-" + projectTaskSequence);
		projectTask.setProjectIdentifier(identifier);
		// Set initial priority
		if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3); // 3- Low, 2- Medium, 1- High
		}
		// Set initial status
		if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}
		return projectTaskRepository.save(projectTask);

	}

	// find all project task of particular backlog
	public List<ProjectTask> findByBacklogId(String projectIdentifier, String username) {
		String identifier = projectIdentifier.toUpperCase();

		projectService.findProjectByIdentifier(identifier, username);
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(identifier);
	}

	// find project task with projectTaskSequence (projectSequence)
	public ProjectTask findPTbyProjectSequence(String backlog_id, String pt_id, String username) {
		String identifier = backlog_id.toUpperCase();
		String ptSequence = pt_id.toUpperCase();

		projectService.findProjectByIdentifier(identifier, username);

		// make sure that project task exist
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptSequence);
		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task '" + ptSequence + "' Doesn't Exist");
		}

		// make sure that project task is associated with backlog
		if (!projectTask.getProjectIdentifier().equals(identifier)) {
			throw new ProjectNotFoundException(
					"Project Task '" + ptSequence + "' doesn't exist in Project: " + identifier);
		}
		return projectTask;
	}

	// update project task
	public ProjectTask updateProjectTask(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
		String identifier = backlog_id.toUpperCase();
		String ptSequence = pt_id.toUpperCase();
		// get existing project task
		ProjectTask projectTask = findPTbyProjectSequence(identifier, ptSequence, username);
		projectTask = updatedTask;
		return projectTaskRepository.save(projectTask);
	}

	// delete project task
	public void deleteProjectTask(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTbyProjectSequence(backlog_id.toUpperCase(), pt_id.toUpperCase(), username);
		projectTaskRepository.delete(projectTask);
	}
}
