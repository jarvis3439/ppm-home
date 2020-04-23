package com.jarvis.ppm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.ppm.exception.ProjectIdException;
import com.jarvis.ppm.model.Backlog;
import com.jarvis.ppm.model.Project;
import com.jarvis.ppm.repository.BacklogRepository;
import com.jarvis.ppm.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;

	// save or update project
	public Project saveOrUpdateProject(Project project) {
		String identifier = project.getProjectIdentifier().toUpperCase();
		try {
			project.setProjectIdentifier(identifier);

			// Setup backlog for project
			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(identifier);
			} else {
				project.setBacklog(backlogRepository.findByProjectIdentifier(identifier));
			}

			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Project Identifier '" + project.getProjectIdentifier().toUpperCase() + "' already exist");
		}
	}

	// find project by projectIdentifer
	public Project findProjectByIdentifier(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project Identifier '" + projectIdentifier.toUpperCase() + "' doesn't exist");
		}
		return project;
	}

	// find all project
	public List<Project> findAllProjects() {
		return projectRepository.findAll();
	}

	// delete project by identifier
	public void deleteProjectByIdentifier(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project Identifier '" + projectIdentifier + "' doesn't exist");
		}
		projectRepository.delete(project);
	}

}
