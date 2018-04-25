package com.sharaga.markstudents.server.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "teacher_SSTU")
public class TeacherFromSSTU {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name = "own_rasp")
    private String ownPage;

    @Column(name = "id_rasp")
    private long idPage;

    public TeacherFromSSTU(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOwnPage() {
        return ownPage;
    }

    public void setOwnPage(String ownPage) {
        this.ownPage = ownPage;
    }

    public long getIdPage() {
        return idPage;
    }

    public void setIdPage(long idPage) {
        this.idPage = idPage;
    }
}
