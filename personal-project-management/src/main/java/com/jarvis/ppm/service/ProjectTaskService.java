package com.jarvis.ppm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.ppm.model.Backlog;
import com.jarvis.ppm.model.ProjectTask;
import com.jarvis.ppm.repository.BacklogRepository;
import com.jarvis.ppm.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

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
	}

}
