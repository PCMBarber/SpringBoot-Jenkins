package com.qa.tdl.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.tdl.persistence.domain.TaskDomain;
import com.qa.tdl.persistence.dtos.AssigneeDTO;
import com.qa.tdl.persistence.dtos.TaskDTO;
import com.qa.tdl.persistence.repos.AssigneeRepo;
import com.qa.tdl.persistence.repos.TaskRepo;

@Service
public class TaskService {

	private TaskRepo repo;
	private AssigneeRepo assigneeRepo;
	private ModelMapper mapper;

	@Autowired
	public TaskService(TaskRepo repo, AssigneeRepo assigneeRepo, ModelMapper mapper) {
		this.repo = repo;
		this.assigneeRepo = assigneeRepo;
		this.mapper = mapper;
	}

	private TaskDTO mapToDto(TaskDomain model) {
		TaskDTO dto = this.mapper.map(model, TaskDTO.class);
		dto.setAssignees(model.getAssignees().stream().map(s -> this.mapper.map(s, AssigneeDTO.class))
				.collect(Collectors.toSet()));
		return dto;
	}

	// POST
	public TaskDTO create(TaskDomain model) {
		return model.getDateTimeSet() == null
				? this.mapToDto(this.repo
						.save(new TaskDomain(model.getTitle(), model.getCompleted(), Timestamp.from(Instant.now()))))
				: this.mapToDto(
						this.repo.save(new TaskDomain(model.getTitle(), model.getCompleted(), model.getDateTimeSet())));
	}

	// GET
	public List<TaskDTO> readAll() {
		return this.repo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	public TaskDTO readTask(long id) {
		return this.mapToDto(this.repo.findById(id).orElseThrow());
	}

	// DELETE
	public boolean delete(long id) {
		Optional<TaskDomain> existingOptional = this.repo.findById(id);

		if (existingOptional.isPresent()) {
			TaskDomain existing = existingOptional.get();
			existing.emptyAssignees(); // so that any of the assigned don't get deleted as well when deleteById gets
										// called
			this.repo.deleteById(id);
			return !(this.repo.existsById(id));
		}

		return false;
	}

	// PUT
	public TaskDTO update(long id, TaskDomain model) {
		Optional<TaskDomain> oc = this.repo.findById(id);
		TaskDomain existing = oc.orElseThrow();

		if (model.getTitle() != null) {
			existing.setTitle(model.getTitle());
		}
		if (model.getCompleted() != null) {
			existing.setCompleted(model.getCompleted());
		}

		return this.mapToDto(this.repo.save(existing));
	}

	public TaskDTO addAssignee(long id, long assigneeId) {
		Optional<TaskDomain> oc = this.repo.findById(id);
		TaskDomain existing = oc.orElseThrow();

		existing.addAssignee(this.assigneeRepo.findById(assigneeId).orElseThrow());

		return this.mapToDto(this.repo.save(existing));
	}

	public TaskDTO removeAssignee(long id, long assigneeId) {
		Optional<TaskDomain> oc = this.repo.findById(id);
		TaskDomain existing = oc.orElseThrow();

		existing.removeAssignee(this.assigneeRepo.findById(assigneeId).orElseThrow());

		return this.mapToDto(this.repo.save(existing));
	}

}
