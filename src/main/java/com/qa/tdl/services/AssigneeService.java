package com.qa.tdl.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.tdl.persistence.domain.AssigneeDomain;
import com.qa.tdl.persistence.dtos.AssigneeDTO;
import com.qa.tdl.persistence.repos.AssigneeRepo;

@Service
public class AssigneeService {

	private AssigneeRepo repo;
	private ModelMapper mapper;
	
	@Autowired
	public AssigneeService(AssigneeRepo repo, ModelMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}
	
	private AssigneeDTO mapToDto(AssigneeDomain model) {
		return this.mapper.map(model, AssigneeDTO.class);
	}
	
	// POST
	public AssigneeDTO create(AssigneeDomain model) {
		return  this.mapToDto(this.repo.save(model));
	}

	// GET
	public List<AssigneeDTO> readAll() {
		return this.repo.findAll().stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public AssigneeDTO readAssignee(long id) {
		return this.mapToDto(this.repo.findById(id).orElseThrow());
	}

	// DELETE
	public boolean delete(long id) {
		Optional<AssigneeDomain> existingOptional = this.repo.findById(id);
		
		if (existingOptional.isPresent()) {
			AssigneeDomain existing = existingOptional.get();
			existing.removeTasks();
			this.repo.deleteById(id);
			return !(this.repo.existsById(id));
		}
		
		return false;
	}

	// PUT
	public AssigneeDTO update(long id, AssigneeDomain model) {
		Optional<AssigneeDomain> oc = this.repo.findById(id);
		AssigneeDomain existing = oc.orElseThrow();

		existing.setName(model.getName());

		return this.mapToDto(this.repo.save(existing));
	}
	
}
