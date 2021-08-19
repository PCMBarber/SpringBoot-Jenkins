package com.qa.tdl.persistence.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.tdl.persistence.domain.AssigneeDomain;

@Repository
public interface AssigneeRepo extends JpaRepository<AssigneeDomain, Long> {

}
