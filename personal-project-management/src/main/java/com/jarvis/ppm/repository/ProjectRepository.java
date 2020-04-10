package com.jarvis.ppm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jarvis.ppm.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	public Project findByProjectIdentifier(String projectIdentifier);

}
