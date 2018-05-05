package com.sharaga.markstudents.server.dto;

import com.sharaga.markstudents.server.entity.Lesson;

import java.util.ArrayList;
import java.util.List;

public class ArrayLessonDTO {

    private List<Lesson> lessons = new ArrayList<>();

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
