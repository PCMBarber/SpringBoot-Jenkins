package com.qa.tdl.rest;


import java.util.List;

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
import com.qa.tdl.persistence.dtos.AssigneeDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:schema-test.sql", "classpath:data-test.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles(profiles = "test")
class AssigneeControllerIntegrationTest {
	
	@Autowired
	private MockMvc mock;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ObjectMapper jsonifier;
	
	private Long id = 1L;
	private final String URL = "http://localhost:8080/assignee/";
	
	private AssigneeDTO mapToDto(AssigneeDomain model) {
		return this.mapper.map(model, AssigneeDTO.class);
	}
	
	@Test
	void readAll() throws Exception {
		List<AssigneeDTO> expectedResult = List.of(
				new AssigneeDTO(1L, "Jane"),
				new AssigneeDTO(2L, "Bob"),
				new AssigneeDTO(3L, "Paul"),
				new AssigneeDTO(4L, "Sally"));
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, URL + "read/all");
		
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content().json(jsonifier.writeValueAsString(expectedResult));
		
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	void readAssignee() throws Exception {
		// resources
		AssigneeDTO expectedResult = new AssigneeDTO(id, "Jane");
		
		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.request(HttpMethod.GET, URL + "read/" + id);
		
		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(jsonifier.writeValueAsString(expectedResult));
		
		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	// POST
	@Test
	void create() throws Exception {
		// resources
		AssigneeDomain contentBody = new AssigneeDomain(4L, "Porter");
		AssigneeDTO expectedResult = this.mapToDto(contentBody);
		
		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
					.request(HttpMethod.POST, URL + "create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonifier.writeValueAsString(contentBody))
					.accept(MediaType.APPLICATION_JSON);
		
		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isCreated();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(jsonifier.writeValueAsString(expectedResult));
		
		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	// PUT
	@Test
	void updateAssignee() throws Exception {
		// resources
		AssigneeDomain contentBody = new AssigneeDomain(2L, "Travis");
		AssigneeDTO expectedResult = this.mapToDto(contentBody);
		
		// set up request
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
					.request(HttpMethod.PUT, URL + "update?id=2")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonifier.writeValueAsString(contentBody))
					.accept(MediaType.APPLICATION_JSON);
		
		// set up expectations
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(jsonifier.writeValueAsString(expectedResult));
		
		// perform
		this.mock.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	// DELETE
	@Test
	void removeAssignee() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.request(HttpMethod.DELETE, URL + "delete/" + id);
		
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isNoContent();
		
		this.mock.perform(mockRequest).andExpect(matchStatus);
	}
	
	@Test
	void removeAssigneeFailure() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.request(HttpMethod.DELETE, URL + "delete/" + 7L);
		
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isInternalServerError();
		
		this.mock.perform(mockRequest).andExpect(matchStatus);
	}
}
