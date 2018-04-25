package com.sharaga.markstudents.server.controller;

import com.sharaga.markstudents.server.entity.Group;
import com.sharaga.markstudents.server.entity.Subject;
import com.sharaga.markstudents.server.entity.Teacher;
import com.sharaga.markstudents.server.repository.SubjectRepository;
import com.sharaga.markstudents.server.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/reminder")
public class RemindController {

    @Autowired
    private TeacherService service;

    @Autowired
    private SubjectRepository subjectRepository;
    @RequestMapping(value = "/reminders", method = RequestMethod.GET)
    @ResponseBody
    public List<Subject> getAllReminders() {
        AddT();
        //Teacher kek = service.getByID(1);
        return subjectRepository.findAll();
    }

    @RequestMapping(value = "/Teacher", method = RequestMethod.GET)
    @ResponseBody
    public Teacher getTeacher() {
        AddT();
        Teacher kek = service.getByID(1);

        return kek;
    }



    @RequestMapping(value = "/teat", method = RequestMethod.GET)
    @ResponseBody
    public List<Subject> getAllTeacher() {
        AddT();
        //Teacher kek = service.getByID(1);
        return subjectRepository.findAll();
    }

    @RequestMapping(value = "/reminders/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Teacher getReminder(@PathVariable("id") long remindID) {
        return service.getByID(remindID);
    }

    @RequestMapping(value = "/reminders", method = RequestMethod.POST)
    @ResponseBody
    public Teacher saveRemider(@RequestBody Teacher remind) {
        return service.save(remind);
    }

    @RequestMapping(value = "/reminders/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable long id) {
        service.remove(id);
    }

    private void AddT(){
        Teacher n = new Teacher();
        n.setMiddleName("ha");
        n.setLastName("ga");
        n.setFirstName("fafafaf");
        Subject s = new Subject();
        s.setTitle("hui");
        s.setType("prak");
        Group g = new Group();
        g.setName("ga");
        g.setIdRasp(2);
        s.addGroup(g);
        n.addSubject(s);
        Subject s2 = new Subject();
        n.addSubject(s2);
        service.save(n);

    }
}
