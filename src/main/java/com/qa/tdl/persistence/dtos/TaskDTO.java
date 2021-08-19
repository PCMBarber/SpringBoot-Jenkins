package com.qa.tdl.persistence.dtos;

import java.sql.Timestamp;
import java.util.Set;

public class TaskDTO {

	private Long id;
	private String title;
	private Boolean completed;
	private Timestamp dateTimeSet;
	private Set<AssigneeDTO> assignees;

	public TaskDTO() {
		super();
	}

	public TaskDTO(Long id, String title, Boolean completed, Timestamp dateTimeSet, Set<AssigneeDTO> assignees) {
		super();
		this.id = id;
		this.title = title;
		this.completed = completed;
		this.dateTimeSet = dateTimeSet;
		this.assignees = assignees;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Timestamp getDateTimeSet() {
		return dateTimeSet;
	}

	public void setDateTimeSet(Timestamp dateTimeSet) {
		this.dateTimeSet = dateTimeSet;
	}
	
	public Set<AssigneeDTO> getAssignees() {
		return assignees;
	}
	
	public void setAssignees(Set<AssigneeDTO> assignees) {
		this.assignees = assignees;
	}

	@Override
	public String toString() {
		return "TaskDTO [id=" + id + ", title=" + title + ", completed=" + completed + ", dateTimeSet=" + dateTimeSet
				+ "]";
	}
	
}
