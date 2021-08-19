package com.qa.tdl.rest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.tdl.persistence.domain.AssigneeDomain;
import com.qa.tdl.persistence.domain.TaskDomain;
import com.qa.tdl.persistence.dtos.AssigneeDTO;
import com.qa.tdl.persistence.dtos.TaskDTO;
import com.qa.tdl.services.TaskService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TaskControllerUnitTest {

	@MockBean
	private TaskService service;

	@Autowired
	private TaskController controller;

	@Test
	void readAllTest() {
		TaskDTO TEST_DTO1 = new TaskDTO(1L, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"),
				Set.of(new AssigneeDTO(1L, "Jane")));
		TaskDTO TEST_DTO2 = new TaskDTO(2L, "Make coffee", false, Timestamp.valueOf("2021-01-21 13:00:00"),
				Set.of(new AssigneeDTO(2L, "Bob"), new AssigneeDTO(3L, "Paul")));
		TaskDTO TEST_DTO3 = new TaskDTO(3L, "Take out bins", true, Timestamp.valueOf("2020-12-30 19:00:00"),
				Set.of(new AssigneeDTO(3L, "Paul")));
		TaskDTO TEST_DTO4 = new TaskDTO(4L, "Buy masks", false, Timestamp.valueOf("2021-02-01 03:30:00"), Set.of());
		List<TaskDTO> DTO_LIST = List.of(TEST_DTO1, TEST_DTO2, TEST_DTO3, TEST_DTO4);

		Mockito.when(this.service.readAll()).thenReturn(DTO_LIST);

		ResponseEntity<List<TaskDTO>> result = this.controller.readAll();

		Assertions.assertThat(result).isEqualTo(ResponseEntity.ok(DTO_LIST));
	}

	@Test
	void readTaskTest() {
		Long id = 1L;
		TaskDTO TEST_DTO = new TaskDTO(id, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"),
				Set.of(new AssigneeDTO(1L, "Jane")));

		Mockito.when(this.service.readTask(id)).thenReturn(TEST_DTO);

		ResponseEntity<TaskDTO> result = this.controller.readTask(id);

		Assertions.assertThat(result).isEqualTo(ResponseEntity.ok(TEST_DTO));
	}

	@Test
	void createTest() {
		TaskDomain TEST_TASK = new TaskDomain(5L, "Food shopping", false, Timestamp.from(Instant.now()));
		TaskDTO TEST_DTO = new TaskDTO(TEST_TASK.getId(), TEST_TASK.getTitle(), TEST_TASK.getCompleted(),
				TEST_TASK.getDateTimeSet(), null);

		Mockito.when(this.service.create(TEST_TASK)).thenReturn(TEST_DTO);

		ResponseEntity<TaskDTO> result = this.controller.create(TEST_TASK);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<TaskDTO>(TEST_DTO, HttpStatus.CREATED));
	}

	@Test
	void updateTaskTest() {
		Long id = 1L;
		TaskDomain TEST_TASK_UPDATE = new TaskDomain(id, "Buy pens", false, Timestamp.from(Instant.now()),
				Set.of(new AssigneeDomain(1L, "Jane", null)));
		TaskDTO TEST_DTO_UPDATE = new TaskDTO(TEST_TASK_UPDATE.getId(), TEST_TASK_UPDATE.getTitle(),
				TEST_TASK_UPDATE.getCompleted(), TEST_TASK_UPDATE.getDateTimeSet(),
				Set.of(new AssigneeDTO(1L, "Jane")));

		Mockito.when(this.service.update(id, TEST_TASK_UPDATE)).thenReturn(TEST_DTO_UPDATE);

		ResponseEntity<TaskDTO> result = this.controller.updateTask(id, TEST_TASK_UPDATE);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<TaskDTO>(TEST_DTO_UPDATE, HttpStatus.ACCEPTED));
	}

	@Test
	void addAssigneeTest() {
		Long id = 4L;
		Long assigneeId = 2L;
		TaskDTO TEST_DTO_UPDATE = new TaskDTO(id, "Buy masks", false, Timestamp.valueOf("2021-02-01 03:30:00"),
				Set.of(new AssigneeDTO(assigneeId, "Bob")));

		Mockito.when(this.service.addAssignee(id, assigneeId)).thenReturn(TEST_DTO_UPDATE);

		ResponseEntity<TaskDTO> result = this.controller.addAssignee(id, assigneeId);

		System.out.println(result);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<TaskDTO>(TEST_DTO_UPDATE, HttpStatus.ACCEPTED));
	}

	@Test
	void removeAssigneeTest() {
		Long id = 2L;
		Long assigneeIdRemove = 3L;
		TaskDTO TEST_DTO_UPDATE = new TaskDTO(id, "Make Coffee", false, Timestamp.valueOf("2021-02-01 03:30:00"),
				Set.of(new AssigneeDTO(2L, "Bob")));

		Mockito.when(this.service.removeAssignee(id, assigneeIdRemove)).thenReturn(TEST_DTO_UPDATE);

		ResponseEntity<TaskDTO> result = this.controller.removeAssignee(id, assigneeIdRemove);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<TaskDTO>(TEST_DTO_UPDATE, HttpStatus.ACCEPTED));
	}

	@Test
	void removeTaskSuccessfulTest() {
		long id = 1L;
		Mockito.when(this.service.delete(id)).thenReturn(true);

		ResponseEntity<Object> result = this.controller.removeTask(id);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}

	@Test
	void removeTaskUnsuccessfulTest() {
		long id = 1L;
		Mockito.when(this.service.delete(id)).thenReturn(false);

		ResponseEntity<Object> result = this.controller.removeTask(id);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}
}
