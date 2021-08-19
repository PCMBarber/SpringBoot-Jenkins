package com.qa.tdl.persistence.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskDTOUnitTest {

	private TaskDTO task;
	private AssigneeDTO jane;

	@BeforeEach
	void setUp() {
		Set<AssigneeDTO> assignees = new HashSet<>();
		jane = new AssigneeDTO(1L, "Jane");
		assignees.add(jane);
		
		task = new TaskDTO(1L, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"), assignees);
	}

	@Test
	void settersTest() {
		assertNotNull(task.getId());
		assertNotNull(task.getTitle());
		assertNotNull(task.getCompleted());
		assertNotNull(task.getDateTimeSet());
		assertNotNull(task.getAssignees());
		
		task.setId(null);
		assertNull(task.getId());
		task.setTitle(null);
		assertNull(task.getTitle());
		task.setCompleted(null);
		assertNull(task.getCompleted());
		task.setDateTimeSet(null);
		assertNull(task.getDateTimeSet());
		task.setAssignees(null);
		assertNull(task.getAssignees());
	}

	@Test
	void createTaskDTOWithId() {
		assertEquals(1L, task.getId(), 0);
		assertEquals("Do laundry", task.getTitle());
		assertEquals(false, task.getCompleted());
		assertEquals(Timestamp.valueOf("2021-02-05 08:00:00"), task.getDateTimeSet());
		Assertions.assertThat(Set.of(new AssigneeDTO(1L, "Jane"))).usingRecursiveComparison().isEqualTo(task.getAssignees());
	}
	
	@Test
	void emptyConstructor() {
		TaskDTO task = new TaskDTO();
		assertNull(task.getId());
		assertNull(task.getTitle());
		assertNull(task.getCompleted());
		assertNull(task.getDateTimeSet());
		assertNull(task.getAssignees());
	}
	
	@Test
	void toStringTest() {
		String toString = "TaskDTO [id=1, title=Do laundry, completed=false, dateTimeSet=2021-02-05 08:00:00.0]";
		assertEquals(toString, task.toString());
	}
	
}
