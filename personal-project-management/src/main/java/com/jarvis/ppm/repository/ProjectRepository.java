package com.jarvis.ppm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jarvis.ppm.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	public Project findByProjectIdentifier(String projectIdentifier);

	List<Project> findByProjectLeader(String username);

}
