package com.jarvis.ppm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Backlog class used to couple ProjectTask list with Project. 
 * So we have to call Backlog object who contain the ProjectTask.
 * This make loading of Project object more efficient when we only need to load 
 * to Project information not everything that associated with it.
 */

@Entity
public class Backlog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Integer PTSequence = 0; // ProjectTask sequence in each backlog
	private String projectIdentifier; // same identifier as project

	/*
	 * OneToOne relationship with project Each Project has one and only one backlog
	 * One Project = One Backlog
	 */

	/*
	 * OneToMany with project tasks One backlog can have one or many project task
	 * Backlog (One) = (ProjectTask >= 1)
	 */
	public Backlog() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getPTSequence() {
		return PTSequence;
	}

	public void setPTSequence(Integer pTSequence) {
		PTSequence = pTSequence;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

}
