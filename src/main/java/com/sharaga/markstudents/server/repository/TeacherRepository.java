package com.sharaga.markstudents.server.repository;

import com.sharaga.markstudents.server.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher getById(long id);
    Teacher getByLogin(String login);

}
