package com.jarvis.ppm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.ppm.exception.ProjectIdException;
import com.jarvis.ppm.exception.ProjectNotFoundException;
import com.jarvis.ppm.model.Backlog;
import com.jarvis.ppm.model.Project;
import com.jarvis.ppm.model.User;
import com.jarvis.ppm.repository.BacklogRepository;
import com.jarvis.ppm.repository.ProjectRepository;
import com.jarvis.ppm.repository.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private UserRepository userRepository;

	// Save or Update project
	public Project saveOrUpdateProject(Project project, String username) {
		String identifier = project.getProjectIdentifier().toUpperCase();

		if (project.getId() != null) {
			Project existingProject = projectRepository.findByProjectIdentifier(identifier);
			if (existingProject != null && (!project.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundException("Project Doesn't belong to your account");
			} else if(existingProject == null) {
				throw new ProjectNotFoundException("Project cannot be updated because it doesn't exist");
			}
		}

		try {
			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(identifier);

			// Setup backlog for new project
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
			throw new ProjectIdException("Project Identifier '" + identifier + "' already exist");
		}
	}

	// find project by projectIdentifier
	public Project findProjectByIdentifier(String projectIdentifier, String username) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project Identifier '" + projectIdentifier.toUpperCase() + "' doesn't exist");
		}
		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project Doesn't belong to your account");
		}
		return project;

	}

	// find all projects of particular user
	public List<Project> findAllProjects(String username) {
		return projectRepository.findByProjectLeader(username);
	}

	// delete project by identifier
	public void deleteProjectByIdentifier(String projectIdentifier, String username) {

		projectRepository.delete(findProjectByIdentifier(projectIdentifier, username));
	}

}
