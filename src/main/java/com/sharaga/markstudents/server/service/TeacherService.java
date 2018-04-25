package com.sharaga.markstudents.server.service;

import com.sharaga.markstudents.server.entity.Teacher;
import com.sharaga.markstudents.server.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService implements ITeacherService {

    @Autowired
    private TeacherRepository repository;

    public List<Teacher> getAll() {
        return repository.findAll();
    }

    public Teacher getByID(long id) {
        return repository.getById(id);
    }

    public Teacher save(Teacher teacher) {
        return repository.saveAndFlush(teacher);
    }

    public void remove(long id) {
        repository.deleteById(id);
    }
}
