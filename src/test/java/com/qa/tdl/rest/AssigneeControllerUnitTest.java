package com.qa.tdl.rest;

import java.util.List;

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
import com.qa.tdl.persistence.dtos.AssigneeDTO;
import com.qa.tdl.services.AssigneeService;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class AssigneeControllerUnitTest {

	@MockBean
	private AssigneeService service;

	@Autowired
	private AssigneeController controller;

	@Test
	void readAllTest() {
		AssigneeDTO TEST_DTO1 = new AssigneeDTO(1L, "Jane");
		AssigneeDTO TEST_DTO2 = new AssigneeDTO(2L, "Bob");
		AssigneeDTO TEST_DTO3 = new AssigneeDTO(3L, "Paul");
		List<AssigneeDTO> DTO_LIST = List.of(TEST_DTO1, TEST_DTO2, TEST_DTO3);

		Mockito.when(this.service.readAll()).thenReturn(DTO_LIST);

		ResponseEntity<List<AssigneeDTO>> result = this.controller.readAll();

		Assertions.assertThat(result).isEqualTo(ResponseEntity.ok(DTO_LIST));
	}

	@Test
	void readAssigneeTest() {
		Long id = 1L;
		AssigneeDTO TEST_DTO = new AssigneeDTO(id, "Jane");

		Mockito.when(this.service.readAssignee(id)).thenReturn(TEST_DTO);

		ResponseEntity<AssigneeDTO> result = this.controller.readAssignee(id);

		Assertions.assertThat(result).isEqualTo(ResponseEntity.ok(TEST_DTO));
	}

	@Test
	void createTest() {
		AssigneeDomain TEST_ASSIGNEE = new AssigneeDomain(4L, "Porter");
		AssigneeDTO TEST_DTO = new AssigneeDTO(TEST_ASSIGNEE.getId(), TEST_ASSIGNEE.getName());

		Mockito.when(this.service.create(TEST_ASSIGNEE)).thenReturn(TEST_DTO);

		ResponseEntity<AssigneeDTO> result = this.controller.create(TEST_ASSIGNEE);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<AssigneeDTO>(TEST_DTO, HttpStatus.CREATED));
	}

	@Test
	void updateAssigneeTest() {
		Long id = 1L;
		AssigneeDomain TEST_ASSIGNEE_UPDATE = new AssigneeDomain(id, "Travis");
		AssigneeDTO TEST_DTO_UPDATE = new AssigneeDTO(id, TEST_ASSIGNEE_UPDATE.getName());

		Mockito.when(this.service.update(id, TEST_ASSIGNEE_UPDATE)).thenReturn(TEST_DTO_UPDATE);

		ResponseEntity<AssigneeDTO> result = this.controller.updateAssignee(id, TEST_ASSIGNEE_UPDATE);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<AssigneeDTO>(TEST_DTO_UPDATE, HttpStatus.ACCEPTED));
	}

	@Test
	void removeAssigneeSuccessfulTest() {
		long id = 1L;
		Mockito.when(this.service.delete(id)).thenReturn(true);

		ResponseEntity<Object> result = this.controller.removeAssignee(id);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}

	@Test
	void removeAssigneeUnsuccessfulTest() {
		long id = 1L;
		Mockito.when(this.service.delete(id)).thenReturn(false);

		ResponseEntity<Object> result = this.controller.removeAssignee(id);

		Assertions.assertThat(result).isEqualTo(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}
}
