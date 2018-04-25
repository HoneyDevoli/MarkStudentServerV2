package com.sharaga.markstudents.server.repository;

import com.sharaga.markstudents.server.entity.Group;
import com.sharaga.markstudents.server.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
