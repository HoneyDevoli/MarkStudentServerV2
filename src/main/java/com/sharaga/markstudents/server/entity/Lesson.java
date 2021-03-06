package com.sharaga.markstudents.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name = "date")
    private String date;

    private String auditorium;

    @Column(name = "number_of_lesson")
    private int numberOfLesson;

    @ManyToOne()
    @JoinColumn(name = "subject_id")
    @JsonBackReference
    private Subject subject;

    //@JsonManagedReference
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinTable(name = "lesson_student", joinColumns = @JoinColumn(name = "lesson_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        student.getLessons().add(this);
    }

    public void removeGroup(Student student) {
        students.remove(student);
        student.getLessons().remove(this);
    }

    public Lesson(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }

    public int getNumberOfLesson() {
        return numberOfLesson;
    }

    public void setNumberOfLesson(int numberOfLesson) {
        this.numberOfLesson = numberOfLesson;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson that = (Lesson) o;

        if (!this.date.equals(that.getDate())) return false;
        if (this.auditorium != null ? !this.auditorium.equals(that.getAuditorium()) : that.getAuditorium() != null) return false;
        if (this.numberOfLesson != that.getNumberOfLesson()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int)id;
        result = 31 * result + (this.date != null ? this.date.hashCode() : 0);
        result = result + (this.auditorium != null ? this.auditorium.hashCode() : 0);
        result = result + numberOfLesson;
        return result;
    }
}
