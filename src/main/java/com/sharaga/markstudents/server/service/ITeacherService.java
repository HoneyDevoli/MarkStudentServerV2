package com.sharaga.markstudents.server.service;



import com.sharaga.markstudents.server.entity.Teacher;

import java.util.List;
import java.util.Optional;

public interface ITeacherService {
    List<Teacher> getAll();
    Teacher getByID(long id);
    Teacher save(Teacher teacher);
    void remove(long id);

}
