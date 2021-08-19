package com.qa.tdl.rest;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.tdl.persistence.domain.AssigneeDomain;
import com.qa.tdl.persistence.dtos.AssigneeDTO;
import com.qa.tdl.services.AssigneeService;

@RestController
@RequestMapping("/assignee")
public class AssigneeController {
	
	private AssigneeService service;
	
	@Autowired
	public AssigneeController(AssigneeService service) {
		super();
		this.service = service;
	}
	
	// GET
	@GetMapping("/read/all")
	public ResponseEntity<List<AssigneeDTO>> readAll() {
		return ResponseEntity.ok(this.service.readAll());
	}
	
	@GetMapping("/read/{id}")
	public ResponseEntity<AssigneeDTO> readAssignee(@PathVariable long id) {
		return ResponseEntity.ok(this.service.readAssignee(id));
	}
	
	// POST
	@PostMapping("/create")
	public ResponseEntity<AssigneeDTO> create(@RequestBody AssigneeDomain model) {
		return new ResponseEntity<>(this.service.create(model), HttpStatus.CREATED);
	}
	
	// PUT
	@PutMapping("/update")
	public ResponseEntity<AssigneeDTO> updateAssignee(@PathParam("id") long id, @RequestBody AssigneeDomain model) {
		return new ResponseEntity<>(this.service.update(id, model), HttpStatus.ACCEPTED);
	}
	
	// DELETE
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> removeAssignee(@PathVariable long id) {
		return new ResponseEntity<>(this.service.delete(id) ? HttpStatus.NO_CONTENT : HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
