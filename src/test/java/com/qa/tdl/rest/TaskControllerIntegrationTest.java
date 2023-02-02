package com.qa.tdl.rest;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.tdl.persistence.domain.AssigneeDomain;
import com.qa.tdl.persistence.domain.TaskDomain;
import com.qa.tdl.persistence.dtos.AssigneeDTO;
import com.qa.tdl.persistence.dtos.TaskDTO;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:schema-test.sql",
		"classpath:data-test.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(profiles = "test")
class TaskControllerIntegrationTest {

	@Autowired
	private MockMvc mock;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ObjectMapper jsonifier;

	private Long id = 1L;
	private final String URL = "http://localhost:8080/task/";

	private TaskDTO mapToDto(TaskDomain model) {
		TaskDTO dto = this.mapper.map(model, TaskDTO.class);
		dto.setAssignees(model.getAssignees().stream().map(s -> this.mapper.map(s, AssigneeDTO.class))
				.collect(Collectors.toSet()));
		return dto;
	}

	@Test
	void readAll() throws Exception {
		// resources
		List<TaskDTO> expectedResult = List.of(
				new TaskDTO(1L, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"), Set.of(new AssigneeDTO(1L, "Jane"))), 
				new TaskDTO(2L, "Make coffee", false, Timestamp.valueOf("2021-01-21 13:00:00"), 
						Set.of(new AssigneeDTO(2L, "Bob"), new AssigneeDTO(3L, "Paul"))),
				new TaskDTO(3L, "Take out bins", true, Timestamp.valueOf("2020-12-30 19:00:00"), Set.of(new AssigneeDTO(2L, "Bob"))),
				new TaskDTO(4L, "Buy masks", false, Timestamp.valueOf("2021-02-01 03:30:00"), Set.of()));
		
		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, URL + "read/all");
		
		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(jsonifier.writeValueAsString(expectedResult));
		
		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}

	@Test
	void readTask() throws Exception {
		// resources
		TaskDTO expectedResult = new TaskDTO(1L, "Do laundry", false, Timestamp.valueOf("2021-02-05 08:00:00"),
				Set.of(new AssigneeDTO(1L, "Jane")));

		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, URL + "read/" + id);

		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(jsonifier.writeValueAsString(expectedResult));

		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}

	// POST
	@Test
	void create() throws Exception {
		// resources
		TaskDomain contentBody = new TaskDomain(5L, "Food shopping", false, Timestamp.valueOf("2021-01-21 13:00:00"));
		TaskDTO expectedResult = this.mapToDto(contentBody);

		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, URL + "create")
				.contentType(MediaType.APPLICATION_JSON).content(jsonifier.writeValueAsString(contentBody))
				.accept(MediaType.APPLICATION_JSON);

		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isCreated();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(jsonifier.writeValueAsString(expectedResult));

		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}

	// PUT
	@Test
	void updateTask() throws Exception {
		// resources
		TaskDomain contentBody = new TaskDomain(2L, "Food shopping", false, Timestamp.valueOf("2021-01-21 13:00:00"),
				Set.of(new AssigneeDomain(2L, "Bob", null), new AssigneeDomain(3L, "Paul", null)));
		TaskDTO expectedResult = this.mapToDto(contentBody);

		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, URL + "update/2")
				.contentType(MediaType.APPLICATION_JSON).content(jsonifier.writeValueAsString(contentBody))
				.accept(MediaType.APPLICATION_JSON);

		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(jsonifier.writeValueAsString(expectedResult));

		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}

	void addAssignee() throws Exception {
		// resources
		TaskDTO expectedResult = this.mapToDto(new TaskDomain(4L, "Buy masks", false,
				Timestamp.valueOf("2021-02-01 03:30:00"), Set.of(new AssigneeDomain(2L, "Bob", null))));

		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT,
				URL + "update/4/add-assignee?assignee_id=2");

		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(jsonifier.writeValueAsString(expectedResult));

		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}

	void removeAssignee() throws Exception {
		// resources
		TaskDTO expectedResult = this.mapToDto(new TaskDomain(2L, "Make coffee", false,
				Timestamp.valueOf("2021-02-01 03:30:00"), Set.of(new AssigneeDomain(2L, "Bob", null))));

		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT,
				URL + "update/2/remove-assignee?assignee_id=3");

		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(jsonifier.writeValueAsString(expectedResult));

		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);

	}

	@Test
	void removeTaskFailure() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE,
				URL + "delete/" + 7L);

		ResultMatcher matchStatus = MockMvcResultMatchers.status().isInternalServerError();

		this.mock.perform(mockRequest).andExpect(matchStatus);
	}
}
