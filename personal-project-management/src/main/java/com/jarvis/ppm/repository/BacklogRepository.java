package com.jarvis.ppm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jarvis.ppm.model.Backlog;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long> {

}
