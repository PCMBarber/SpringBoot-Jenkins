package com.qa.tdl.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.tdl.persistence.domain.AssigneeDomain;
import com.qa.tdl.persistence.dtos.AssigneeDTO;
import com.qa.tdl.persistence.repos.AssigneeRepo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AssigneeServiceUnitTest {
	
	@MockBean
	private AssigneeRepo repo;
	
	@MockBean
	private ModelMapper mapper;
	
	@Autowired
	AssigneeService service;

	@Test
	void createTest() {
		AssigneeDomain TEST_ASSIGNEE = new AssigneeDomain(4L, "Porter");
		AssigneeDTO TEST_DTO = new AssigneeDTO(TEST_ASSIGNEE.getId(), TEST_ASSIGNEE.getName());
		
		Mockito.when(this.repo.save(Mockito.any(AssigneeDomain.class))).thenReturn(TEST_ASSIGNEE);
		Mockito.when(this.mapper.map(TEST_ASSIGNEE, AssigneeDTO.class)).thenReturn(TEST_DTO);
		
		AssigneeDTO result = this.service.create(TEST_ASSIGNEE);
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO);
		
		Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(AssigneeDomain.class));
	}
	
	@Test
	void readAssigneeTest() {
		Long id = 1L;
		AssigneeDomain TEST_ASSIGNEE = new AssigneeDomain(id, "Jane");
		Optional<AssigneeDomain> TEST_OPTIONAL = Optional.of(TEST_ASSIGNEE);
		AssigneeDTO TEST_DTO = new AssigneeDTO(TEST_ASSIGNEE.getId(), TEST_ASSIGNEE.getName());
		
		Mockito.when(this.repo.findById(id)).thenReturn(TEST_OPTIONAL);
		Mockito.when(this.mapper.map(TEST_ASSIGNEE, AssigneeDTO.class)).thenReturn(TEST_DTO);
		
		AssigneeDTO result = this.service.readAssignee(id);
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO);
		
		Mockito.verify(this.repo, Mockito.times(1)).findById(id);
		Mockito.verify(this.mapper, Mockito.times(1)).map(TEST_ASSIGNEE, AssigneeDTO.class);
	}
	
	@Test
	void readAllTest() {
		AssigneeDomain TEST_ASSIGNEE1 = new AssigneeDomain(1L, "Jane");
		AssigneeDomain TEST_ASSIGNEE2 = new AssigneeDomain(2L, "Bob");
		AssigneeDomain TEST_ASSIGNEE3 = new AssigneeDomain(3L, "Paul");
		AssigneeDTO TEST_DTO1 = new AssigneeDTO(TEST_ASSIGNEE1.getId(), TEST_ASSIGNEE1.getName());
		AssigneeDTO TEST_DTO2 = new AssigneeDTO(TEST_ASSIGNEE2.getId(), TEST_ASSIGNEE2.getName());
		AssigneeDTO TEST_DTO3 = new AssigneeDTO(TEST_ASSIGNEE3.getId(), TEST_ASSIGNEE3.getName());
		List<AssigneeDomain> ASSIGNEE_LIST = List.of(TEST_ASSIGNEE1, TEST_ASSIGNEE2, TEST_ASSIGNEE3);
		List<AssigneeDTO> DTO_LIST = List.of(TEST_DTO1, TEST_DTO2, TEST_DTO3);
		
		Mockito.when(this.repo.findAll()).thenReturn(ASSIGNEE_LIST);
		Mockito.when(this.mapper.map(TEST_ASSIGNEE1, AssigneeDTO.class)).thenReturn(TEST_DTO1);
		Mockito.when(this.mapper.map(TEST_ASSIGNEE2, AssigneeDTO.class)).thenReturn(TEST_DTO2);
		Mockito.when(this.mapper.map(TEST_ASSIGNEE3, AssigneeDTO.class)).thenReturn(TEST_DTO3);
		
		List<AssigneeDTO> result = this.service.readAll();
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(DTO_LIST);
		
		Mockito.verify(this.repo, Mockito.times(1)).findAll();
	}
	
	@Test
	void deleteTest() {
		Mockito.when(this.repo.findById(1L)).thenReturn(
				Optional.of(new AssigneeDomain(1L, "Jane", new HashSet<>())));
		
		this.service.delete(1L);
		
		Mockito.verify(this.repo, Mockito.times(1)).deleteById(1L);
	}
	
	@Test
	void updateTest() {
		Long id = 1L;
		AssigneeDomain TEST_ASSIGNEE = new AssigneeDomain(id, "Jane", null);
		AssigneeDomain TEST_ASSIGNEE_UPDATE = new AssigneeDomain(id, "Travis", null);
		Optional<AssigneeDomain> TEST_OPTIONAL = Optional.of(TEST_ASSIGNEE);
		AssigneeDTO TEST_DTO_UPDATE = new AssigneeDTO(TEST_ASSIGNEE_UPDATE.getId(),TEST_ASSIGNEE_UPDATE.getName());
	
		Mockito.when(this.repo.findById(id)).thenReturn(TEST_OPTIONAL);
		Mockito.when(this.repo.save(Mockito.any(AssigneeDomain.class))).thenReturn(TEST_ASSIGNEE_UPDATE);
		Mockito.when(this.mapper.map(TEST_ASSIGNEE_UPDATE, AssigneeDTO.class)).thenReturn(TEST_DTO_UPDATE);
		
		AssigneeDTO result = this.service.update(id, TEST_ASSIGNEE_UPDATE);
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(TEST_DTO_UPDATE);
		
		Mockito.verify(this.repo, Mockito.times(1)).findById(id);
		Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(AssigneeDomain.class));
	}
}
