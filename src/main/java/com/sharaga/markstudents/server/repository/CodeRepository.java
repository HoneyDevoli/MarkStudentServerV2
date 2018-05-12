package com.sharaga.markstudents.server.repository;

import com.sharaga.markstudents.server.entity.Code;
import com.sharaga.markstudents.server.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code,Long>{

    List<Code> findAllByCode(String code);
    List<Code> findAllByTeacher(Teacher teacher);
}
