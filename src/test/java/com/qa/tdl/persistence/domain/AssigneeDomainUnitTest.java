package com.qa.tdl.persistence.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssigneeDomainUnitTest {

	private AssigneeDomain assignee;
	private TaskDomain task;

	@BeforeEach
	void setUp() {
		Set<TaskDomain> tasks = new HashSet<>();
		task = new TaskDomain(1L, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"));
		tasks.add(task);
		
		assignee = new AssigneeDomain(1L, "Jane", tasks);
		task.addAssignee(assignee);
	}

	@Test
	void settersTest() {
		assertNotNull(assignee.getId());
		assertNotNull(assignee.getName());
		assertNotNull(assignee.getTasks());
		
		assignee.setId(null);
		assertNull(assignee.getId());
		assignee.setName(null);
		assertNull(assignee.getName());
		assignee.setTasks(null);
		assertNull(assignee.getTasks());
	}
	
	@Test
	void removeTasks() {
		assignee.removeTasks();
		Assertions.assertThat(Set.of()).usingRecursiveComparison().isEqualTo(assignee.getTasks());
		Assertions.assertThat(Set.of()).usingRecursiveComparison().isEqualTo(task.getAssignees());
	}

	@Test
	void createAssigneeDomainWithId() {
		assertEquals(1L, assignee.getId(), 0);
		assertEquals("Jane", assignee.getName());
		Assertions.assertThat(Set.of(new TaskDomain(1L, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"), Set.of(assignee))))
			.usingRecursiveComparison().isEqualTo(assignee.getTasks());
	}

	@Test
	void constructorWithoutId() {
		AssigneeDomain assignee = new AssigneeDomain("Jane");
		assertNull(assignee.getId());
		assertNotNull(assignee.getName());
		assertNotNull(assignee.getTasks());
	}
	
	@Test
	void constructorWithoutTasks() {
		AssigneeDomain assignee = new AssigneeDomain(1L, "Jane");
		assertNotNull(assignee.getId());
		assertNotNull(assignee.getName());
		assertNotNull(assignee.getTasks());
	}
	
	@Test
	void emptyConstructor() {
		AssigneeDomain assignee = new AssigneeDomain();
		assertNull(assignee.getId());
		assertNull(assignee.getName());
		assertNull(assignee.getTasks());
	}
	
	@Test
	void toStringTest() {
		String toString = "AssigneeDomain [id=1, name=Jane]";
		assertEquals(toString, assignee.toString());
	}

}
