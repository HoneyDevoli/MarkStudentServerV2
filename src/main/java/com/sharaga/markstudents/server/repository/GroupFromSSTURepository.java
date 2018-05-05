package com.sharaga.markstudents.server.repository;

import com.sharaga.markstudents.server.entity.Group;
import com.sharaga.markstudents.server.entity.GroupFromSSTU;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupFromSSTURepository extends JpaRepository<GroupFromSSTU, Long> {
    GroupFromSSTU getByOwnPage(String name);
}
