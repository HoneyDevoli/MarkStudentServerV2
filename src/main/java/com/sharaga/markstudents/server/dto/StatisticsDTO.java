package com.sharaga.markstudents.server.dto;

import com.sharaga.markstudents.server.entity.Student;
import com.sharaga.markstudents.server.entity.Subject;

import java.util.*;

public class StatisticsDTO {

    private List<Integer> CountLesson = new ArrayList<>();
    private Set<Student> students = new HashSet<>();
    private int totalLesson;
    private Subject subject;

    public void setTotalLesson(int totalLesson) {
        this.totalLesson = totalLesson;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getTotalLesson() {
        return totalLesson;
    }

    public List<Integer> getCountLesson() {
        return CountLesson;
    }

    public void setCountLesson(List<Integer> countLesson) {
        CountLesson = countLesson;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
