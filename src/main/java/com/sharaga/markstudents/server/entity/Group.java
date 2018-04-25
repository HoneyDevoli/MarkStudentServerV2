package com.sharaga.markstudents.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groupUn")
public class Group {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "id_rasp")
    private long idRasp;

    @JsonBackReference
    @ManyToMany(mappedBy = "groups",fetch = FetchType.LAZY)
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        student.setGroup(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setGroup(null);
    }

    public Group(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdRasp() {
        return idRasp;
    }

    public void setIdRasp(long idRasp) {
        this.idRasp = idRasp;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    /*public void addSubject(Subject subject){
        subjects.add(subject);
        subject.getGroups().add(this);
    }

    public void removeSubject(Subject subject){
        subjects.remove(subject);
        subject.getGroups().remove(this);
    }*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group that = (Group) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.getName()) : that.getName() != null) return false;
        if (idRasp != that.getIdRasp()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int)id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
