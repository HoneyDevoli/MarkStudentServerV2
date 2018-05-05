package com.sharaga.markstudents.server.controller;

import com.sharaga.markstudents.server.dto.ArrayGroupDTO;
import com.sharaga.markstudents.server.entity.*;
import com.sharaga.markstudents.server.repository.*;
import com.sharaga.markstudents.server.service.ITeacherService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private GroupFromSSTURepository groupFromSSTURepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TeacherFromSSTURepository teacherFromSSTURepository;

    @RequestMapping(value = "/loginteacher", method = RequestMethod.GET)
    public Teacher loginTeacher(String login,String password) throws IOException {
        return  teacherService.getTeacherAuthorization(login,password);
    }

    @RequestMapping(value = "/loginstudent", method = RequestMethod.GET)
    public Student loginStudent(String login, String password) throws IOException {
        return  getStudentAuthorization(login,password);
    }

    @RequestMapping(value = "/checklogin", method = RequestMethod.GET)
    public Teacher checkLogin(String login) throws IOException {
        Teacher t = new Teacher();
        if(studentRepository.getByLogin(login) == null && teacherRepository.getByLogin(login) == null) {

            t.setLogin(login);
        }
        return t;
    }

    @RequestMapping(value = "/getgroup", method = RequestMethod.GET)
    public ArrayGroupDTO GetGroup() throws IOException {
        ArrayGroupDTO groups = new ArrayGroupDTO();
        groups.setGroups(groupFromSSTURepository.findAll());
        return groups;
    }

    @RequestMapping(value = "/registerstudent", method = RequestMethod.GET)
    public Student registerStudent(String login,String password, String firstname,
                                   String secondname, String lastname, String group) throws IOException {
        Student student = new Student();
        student.setLogin(login);
        student.setPassword(password);
        student.setFirstName(firstname);
        student.setMiddleName(secondname);
        student.setLastName(lastname);
        if(groupRepository.getByName(group) == null){
            Group g = new Group();
            g.setName(group);
            g.setIdRasp(groupFromSSTURepository.getByOwnPage(group).getIdPage());
            g.addStudent(student);
            groupRepository.saveAndFlush(g);
        } else {
            Group g = groupRepository.getByName(group);
            g.addStudent(student);
            groupRepository.saveAndFlush(g);
        }


        return studentRepository.getByLogin(login);
    }

    @RequestMapping(value = "/registerteacher", method = RequestMethod.GET)
    public Teacher registerTeacher(String login,String password, String firstname,
                                   String secondname, String lastname) throws IOException {
        Teacher teacher = new Teacher();
        teacher.setLogin(login);
        teacher.setPassword(password);
        teacher.setFirstName(firstname);
        teacher.setMiddleName(secondname);
        teacher.setLastName(lastname);

        String ownerPage = lastname.replace(lastname.substring(0,1),lastname.substring(0,1).toUpperCase())
                + " " + firstname.substring(0,1).toUpperCase() + secondname.substring(0,1).toUpperCase();
        List<TeacherFromSSTU> tfList = teacherFromSSTURepository.findAll();
        for(TeacherFromSSTU tf : tfList){
           if(tf.getOwnPage().contains(ownerPage))
               teacher.setIdRasp((int) tf.getIdPage());
        }

        for(Subject s : ParseSchedule(teacher)){
            teacher.addSubject(s);
        }
        teacherRepository.saveAndFlush(teacher);



        return teacherRepository.getByLogin(login);
    }


    private Student getStudentAuthorization(String login, String password) {
        Student student = studentRepository.getByLogin(login);
        if (student!=null && student.getPassword().equals(password)) {
            return student;
        } else {
            return null;
        }
    }

    private HashSet<Subject> ParseSchedule(Teacher teacher) throws IOException {
        Document dc;
        dc = Jsoup.connect("http://rasp.sstu.ru/teacher/" + teacher.getIdRasp()).timeout(6000).get();

        HashSet<Subject> subjectsList = new HashSet<Subject>();
        Elements lessions = dc.select("div.rasp-table-inner-cell");
        for (Element lession : lessions) {
            Elements attributes = lession.select("div.small");
            if (attributes.size() > 0) {
                Subject subject = new Subject();
                subject.setTitle(attributes.select("div.subject").text());
                subject.setType(attributes.select("div.type").text());
                subjectsList.add(subject);
            }
        }
        return  subjectsList;
    }
}

