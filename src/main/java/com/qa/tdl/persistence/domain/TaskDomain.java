package com.qa.tdl.persistence.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class TaskDomain {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private Boolean completed;
	
	@Column(nullable = false)
	private Timestamp dateTimeSet;
	
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(
		name = "tasks_assignees",
		joinColumns = { @JoinColumn(name = "task_id") },
		inverseJoinColumns = { @JoinColumn(name = "assignee_id") }
	)
	Set<AssigneeDomain> assignees;

	public TaskDomain() {
		super();
	}

	public TaskDomain(Long id, String title, Boolean completed, Timestamp dateTimeSet, Set<AssigneeDomain> assignees) {
		super();
		this.id = id;
		this.title = title;
		this.completed = completed;
		this.dateTimeSet = dateTimeSet;
		this.assignees = assignees;
	}
	
	public TaskDomain(Long id, String title, Boolean completed, Timestamp dateTimeSet) {
		super();
		this.id = id;
		this.title = title;
		this.completed = completed;
		this.dateTimeSet = dateTimeSet;
		this.assignees = new HashSet<>();
	}
	
	public TaskDomain(String title, Boolean completed, Timestamp dateTimeSet, Set<AssigneeDomain> assignees) {
		super();
		this.title = title;
		this.completed = completed;
		this.dateTimeSet = dateTimeSet;
		this.assignees = assignees;
	}
	
	public TaskDomain(String title, Boolean completed, Timestamp dateTimeSet) {
		super();
		this.title = title;
		this.completed = completed;
		this.dateTimeSet = dateTimeSet;
		this.assignees = new HashSet<>();
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
	
	public Set<AssigneeDomain> getAssignees() {
		return assignees;
	}
	
	public void setAssignees(Set<AssigneeDomain> assignees) {
		this.assignees = assignees;
	}
	
	public void addAssignee(AssigneeDomain assignee) {
		this.assignees.add(assignee);
	}
	
	public void removeAssignee(AssigneeDomain assignee) {
		this.assignees.remove(assignee);
	}
	
	public void emptyAssignees() {
		this.assignees.clear();
	}

	@Override
	public String toString() {
		return "TaskDomain [id=" + id + ", title=" + title + ", completed=" + completed + ", dateTimeSet=" + dateTimeSet
				+ ", assignees=" + assignees + "]";
	}

}
