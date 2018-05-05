package com.sharaga.markstudents.server.repository;

import com.sharaga.markstudents.server.entity.Lesson;
import com.sharaga.markstudents.server.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

    Lesson getById(long id);
}
