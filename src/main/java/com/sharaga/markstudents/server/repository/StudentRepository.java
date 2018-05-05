package com.sharaga.markstudents.server.repository;

import com.sharaga.markstudents.server.entity.Student;
import com.sharaga.markstudents.server.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Student getById(long id);
    Student getByLogin(String login);
}
