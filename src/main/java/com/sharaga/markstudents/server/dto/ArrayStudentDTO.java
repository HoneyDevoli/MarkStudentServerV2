package com.sharaga.markstudents.server.dto;

import com.sharaga.markstudents.server.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class ArrayStudentDTO {

    private List<Student> students = new ArrayList<>();

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}