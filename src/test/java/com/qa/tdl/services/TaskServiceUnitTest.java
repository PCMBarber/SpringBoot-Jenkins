package com.qa.tdl.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.tdl.persistence.domain.AssigneeDomain;
import com.qa.tdl.persistence.domain.TaskDomain;
import com.qa.tdl.persistence.dtos.AssigneeDTO;
import com.qa.tdl.persistence.dtos.TaskDTO;
import com.qa.tdl.persistence.repos.AssigneeRepo;
import com.qa.tdl.persistence.repos.TaskRepo;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class TaskServiceUnitTest {
	
	@MockBean
	private TaskRepo repo;
	
	@MockBean
	private AssigneeRepo assigneeRepo;
	
	@MockBean
	private ModelMapper mapper;
	
	@Autowired
	TaskService service;

	@Test
	void createTest() {
		TaskDomain TEST_TASK = new TaskDomain(5L, "Food shopping", false, Timestamp.from(Instant.now()));
		TaskDTO TEST_DTO = new TaskDTO(TEST_TASK.getId(), TEST_TASK.getTitle(), TEST_TASK.getCompleted(), TEST_TASK.getDateTimeSet(), null);
		
		Mockito.when(this.repo.save(Mockito.any(TaskDomain.class))).thenReturn(TEST_TASK);
		Mockito.when(this.mapper.map(TEST_TASK, TaskDTO.class)).thenReturn(TEST_DTO);
		
		TaskDTO result = this.service.create(TEST_TASK);
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO);
		
		Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(TaskDomain.class));
	}
	
	@Test
	void createTestDateTimeSetNull() {
		TaskDomain TEST_TASK = new TaskDomain(5L, "Food shopping", false, null);
		TaskDTO TEST_DTO = new TaskDTO(TEST_TASK.getId(), TEST_TASK.getTitle(), TEST_TASK.getCompleted(), Timestamp.from(Instant.now()), null);
		
		Mockito.when(this.repo.save(Mockito.any(TaskDomain.class))).thenReturn(TEST_TASK);
		Mockito.when(this.mapper.map(TEST_TASK, TaskDTO.class)).thenReturn(TEST_DTO);
		
		TaskDTO result = this.service.create(TEST_TASK);
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO);
		
		Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(TaskDomain.class));
	}
	
	@Test
	void readTaskTest() {
		Long id = 1L;
		TaskDomain TEST_TASK = new TaskDomain(id, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"), Set.of(new AssigneeDomain(1L, "Jane", null)));
		Optional<TaskDomain> TEST_OPTIONAL = Optional.of(TEST_TASK);
		TaskDTO TEST_DTO = new TaskDTO(TEST_TASK.getId(), TEST_TASK.getTitle(), TEST_TASK.getCompleted(), TEST_TASK.getDateTimeSet(), Set.of(new AssigneeDTO(1L, "Jane")));
		
		Mockito.when(this.repo.findById(id)).thenReturn(TEST_OPTIONAL);
		Mockito.when(this.mapper.map(TEST_TASK, TaskDTO.class)).thenReturn(TEST_DTO);
		
		TaskDTO result = this.service.readTask(id);
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO);
		
		Mockito.verify(this.repo, Mockito.times(1)).findById(id);
		Mockito.verify(this.mapper, Mockito.times(1)).map(TEST_TASK, TaskDTO.class);
	}
	
	@Test
	void readAllTest() {
		TaskDomain TEST_TASK1 = new TaskDomain(1L, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"), Set.of(new AssigneeDomain(1L, "Jane", null)));
		TaskDomain TEST_TASK2 = new TaskDomain(2L, "Make coffee", false, Timestamp.valueOf("2021-01-21 13:00:00"), Set.of(new AssigneeDomain(2L, "Bob", null), new AssigneeDomain(3L, "Paul", null)));
		TaskDomain TEST_TASK3 = new TaskDomain(3L, "Take out bins", true, Timestamp.valueOf("2020-12-30 19:00:00"), Set.of(new AssigneeDomain(3L, "Paul", null)));
		TaskDomain TEST_TASK4 = new TaskDomain(4L, "Buy masks", false, Timestamp.valueOf("2021-02-01 03:30:00"));
		TaskDTO TEST_DTO1 = new TaskDTO(TEST_TASK1.getId(), TEST_TASK1.getTitle(), TEST_TASK1.getCompleted(), TEST_TASK1.getDateTimeSet(), Set.of(new AssigneeDTO(1L, "Jane")));
		TaskDTO TEST_DTO2 = new TaskDTO(TEST_TASK2.getId(), TEST_TASK2.getTitle(), TEST_TASK2.getCompleted(), TEST_TASK2.getDateTimeSet(), Set.of(new AssigneeDTO(2L, "Bob"), new AssigneeDTO(3L, "Paul")));
		TaskDTO TEST_DTO3 = new TaskDTO(TEST_TASK3.getId(), TEST_TASK3.getTitle(), TEST_TASK3.getCompleted(), TEST_TASK3.getDateTimeSet(), Set.of(new AssigneeDTO(3L, "Paul")));
		TaskDTO TEST_DTO4 = new TaskDTO(TEST_TASK4.getId(), TEST_TASK4.getTitle(), TEST_TASK4.getCompleted(), TEST_TASK4.getDateTimeSet(), Set.of());
		List<TaskDomain> TASK_LIST = List.of(TEST_TASK1, TEST_TASK2, TEST_TASK3, TEST_TASK4);
		List<TaskDTO> DTO_LIST = List.of(TEST_DTO1, TEST_DTO2, TEST_DTO3, TEST_DTO4);
		
		Mockito.when(this.repo.findAll()).thenReturn(TASK_LIST);
		Mockito.when(this.mapper.map(TEST_TASK1, TaskDTO.class)).thenReturn(TEST_DTO1);
		Mockito.when(this.mapper.map(TEST_TASK2, TaskDTO.class)).thenReturn(TEST_DTO2);
		Mockito.when(this.mapper.map(TEST_TASK3, TaskDTO.class)).thenReturn(TEST_DTO3);
		Mockito.when(this.mapper.map(TEST_TASK4, TaskDTO.class)).thenReturn(TEST_DTO4);
		
		List<TaskDTO> result = this.service.readAll();
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(DTO_LIST);
		
		Mockito.verify(this.repo, Mockito.times(1)).findAll();
	}
	
	@Test
	void deleteTest() {
		Mockito.when(this.repo.findById(1L)).thenReturn(
				Optional.of(new TaskDomain(1L, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"), new HashSet<>())));
		
		this.service.delete(1L);

		Mockito.verify(this.repo, Mockito.times(1)).deleteById(1L);
	}
	
	@Test
	void updateTest() {
		Long id = 1L;
		TaskDomain TEST_TASK = new TaskDomain(id, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"), Set.of(new AssigneeDomain(1L, "Jane", null)));
		TaskDomain TEST_TASK_UPDATE = new TaskDomain(id, "Buy pens", false, Timestamp.from(Instant.now()), Set.of(new AssigneeDomain(1L, "Jane", null)));
		Optional<TaskDomain> TEST_OPTIONAL = Optional.of(TEST_TASK);
		TaskDTO TEST_DTO_UPDATE = new TaskDTO(TEST_TASK_UPDATE.getId(),TEST_TASK_UPDATE.getTitle(), TEST_TASK_UPDATE.getCompleted(), TEST_TASK_UPDATE.getDateTimeSet(), Set.of(new AssigneeDTO(1L, "Jane")));
	
		Mockito.when(this.repo.findById(id)).thenReturn(TEST_OPTIONAL);
		Mockito.when(this.repo.save(Mockito.any(TaskDomain.class))).thenReturn(TEST_TASK_UPDATE);
		Mockito.when(this.mapper.map(TEST_TASK_UPDATE, TaskDTO.class)).thenReturn(TEST_DTO_UPDATE);
		
		TaskDTO result = this.service.update(id, TEST_TASK_UPDATE);
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO_UPDATE);
		
		Mockito.verify(this.repo, Mockito.times(1)).findById(id);
		Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(TaskDomain.class));
	}
	
	@Test
	void addAssigneeTest() {
		Long id = 4L;
		Long assigneeId = 2L;
		
		AssigneeDomain TEST_ASSIGNEE = new AssigneeDomain(assigneeId, "Bob", null);
		Optional<AssigneeDomain> TEST_ASSIGNEE_OPTIONAL = Optional.of(TEST_ASSIGNEE);
		
		TaskDomain TEST_TASK = new TaskDomain(id, "Buy masks", false, Timestamp.valueOf("2021-02-01 03:30:00"), new HashSet<>());
		TaskDomain TEST_TASK_UPDATE = new TaskDomain(id, "Buy masks", false, Timestamp.valueOf("2021-02-01 03:30:00"), Set.of(TEST_ASSIGNEE));
		Optional<TaskDomain> TEST_OPTIONAL = Optional.of(TEST_TASK);
		TaskDTO TEST_DTO_UPDATE = new TaskDTO(TEST_TASK_UPDATE.getId(),TEST_TASK_UPDATE.getTitle(), TEST_TASK_UPDATE.getCompleted(), TEST_TASK_UPDATE.getDateTimeSet(), Set.of(new AssigneeDTO(assigneeId, "Bob")));
	
		Mockito.when(this.repo.findById(id)).thenReturn(TEST_OPTIONAL);
		Mockito.when(this.assigneeRepo.findById(assigneeId)).thenReturn(TEST_ASSIGNEE_OPTIONAL);
		Mockito.when(this.repo.save(Mockito.any(TaskDomain.class))).thenReturn(TEST_TASK_UPDATE);
		Mockito.when(this.mapper.map(TEST_TASK_UPDATE, TaskDTO.class)).thenReturn(TEST_DTO_UPDATE);
		
		TaskDTO result = this.service.addAssignee(id, TEST_ASSIGNEE.getId());
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO_UPDATE);
		
		Mockito.verify(this.repo, Mockito.times(1)).findById(id);
		Mockito.verify(this.assigneeRepo, Mockito.times(1)).findById(assigneeId);
		Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(TaskDomain.class));
	}
	
	@Test
	void removeAssigneeTest() {
		Long id = 2L;
		Long assigneeIdRemove = 3L;
		
		AssigneeDomain TEST_ASSIGNEE_ONE = new AssigneeDomain(2L, "Bob", null);
		AssigneeDomain TEST_ASSIGNEE_TWO = new AssigneeDomain(assigneeIdRemove, "Paul", null);
		Optional<AssigneeDomain> TEST_ASSIGNEE_OPTIONAL = Optional.of(TEST_ASSIGNEE_TWO);
		
		Set<AssigneeDomain> TEST_ASSIGNEES = new HashSet<>();
		TEST_ASSIGNEES.add(TEST_ASSIGNEE_ONE);
		TEST_ASSIGNEES.add(TEST_ASSIGNEE_TWO);
		TaskDomain TEST_TASK = new TaskDomain(id, "Make coffee", false, Timestamp.valueOf("2021-01-21 13:00:00"), TEST_ASSIGNEES);
		TaskDomain TEST_TASK_UPDATE = new TaskDomain(id, "Make Coffee", false, Timestamp.valueOf("2021-02-01 03:30:00"), Set.of(TEST_ASSIGNEE_ONE));
		Optional<TaskDomain> TEST_OPTIONAL = Optional.of(TEST_TASK);
		TaskDTO TEST_DTO_UPDATE = new TaskDTO(TEST_TASK_UPDATE.getId(),TEST_TASK_UPDATE.getTitle(), TEST_TASK_UPDATE.getCompleted(), TEST_TASK_UPDATE.getDateTimeSet(), Set.of(new AssigneeDTO(2L, "Bob")));
	
		Mockito.when(this.repo.findById(id)).thenReturn(TEST_OPTIONAL);
		Mockito.when(this.assigneeRepo.findById(assigneeIdRemove)).thenReturn(TEST_ASSIGNEE_OPTIONAL);
		Mockito.when(this.repo.save(Mockito.any(TaskDomain.class))).thenReturn(TEST_TASK_UPDATE);
		Mockito.when(this.mapper.map(TEST_TASK_UPDATE, TaskDTO.class)).thenReturn(TEST_DTO_UPDATE);
		
		TaskDTO result = this.service.removeAssignee(id, assigneeIdRemove);
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO_UPDATE);
		
		Mockito.verify(this.repo, Mockito.times(1)).findById(id);
		Mockito.verify(this.assigneeRepo, Mockito.times(1)).findById(assigneeIdRemove);
		Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(TaskDomain.class));
	}
}
