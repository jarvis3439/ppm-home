package com.jarvis.ppm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.ppm.model.Project;
import com.jarvis.ppm.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	// save or update project
	public Project saveOrUpdateProject(Project project) {
		return projectRepository.save(project);
	}
}
