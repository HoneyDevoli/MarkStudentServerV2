package com.sharaga.markstudents.server.repository;

import com.sharaga.markstudents.server.entity.Subject;
import com.sharaga.markstudents.server.entity.TeacherFromSSTU;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherFromSSTURepository extends JpaRepository<TeacherFromSSTU, Long> {

    TeacherFromSSTU getByOwnPage(String ownPage);


}
