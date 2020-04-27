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

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		try {
			// Project Task added to specific project (Project != null)
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			// set the Backlog to the ProjectTask
			projectTask.setBacklog(backlog);
			// Project Sequence
			Integer backlogSequence = backlog.getPTSequence();
			// Update Backlog
			backlogSequence++;
			backlog.setPTSequence(backlogSequence);
			// add Sequence to Project Task
			projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			// Set initial priority
			if (projectTask.getPriority() == null) {
				projectTask.setPriority(3); // 3- Low, 2- Medium, 1- High
			}
			// Set initial status
			if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}

			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project not found");
		}
	}

	public List<ProjectTask> findByBacklogId(String id) {
		Project project = projectRepository.findByProjectIdentifier(id);
		if (project == null) {
			throw new ProjectNotFoundException("Project ID '" + id + "' doesn't exist");
		} else {
			return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
		}
	}

	public ProjectTask findPTbyProjectSequence(String backlog_id, String pt_id) {
		// make sure that project backlog exist.
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		if (backlog == null) {
			throw new ProjectNotFoundException("Project '" + backlog_id + "' Doesn't Exist");
		}

		// make sure that project task exist
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' Doesn't Exist");
		}

		// make sure that project task is associated with backlog
		if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' doesn't exist in Project: " + backlog_id);
		}

		return projectTask;
	}
}
