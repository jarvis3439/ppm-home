package com.jarvis.ppm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.ppm.exception.ProjectIdException;
import com.jarvis.ppm.model.Project;
import com.jarvis.ppm.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	// save or update project
	public Project saveOrUpdateProject(Project project) {
		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
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
