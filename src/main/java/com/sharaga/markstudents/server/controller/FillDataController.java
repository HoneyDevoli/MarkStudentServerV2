package com.sharaga.markstudents.server.controller;

import com.sharaga.markstudents.server.entity.GroupFromSSTU;
import com.sharaga.markstudents.server.entity.Subject;
import com.sharaga.markstudents.server.entity.TeacherFromSSTU;
import com.sharaga.markstudents.server.repository.GroupFromSSTURepository;
import com.sharaga.markstudents.server.repository.SubjectRepository;
import com.sharaga.markstudents.server.repository.TeacherFromSSTURepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/data")
public class FillDataController {

    @Autowired
    private TeacherFromSSTURepository teacherRepo;
    @Autowired
    private GroupFromSSTURepository groupRepo;

    @RequestMapping(value = "/fillteacher", method = RequestMethod.GET)
    @ResponseBody
    public String fillDataTeacher() throws IOException {

        String owner;
        for (int id = 1; id < 4000; id++) {

            Document dc = Jsoup.connect("http://rasp.sstu.ru/teacher/" + id).timeout(6000).get();
            owner = dc.select("div.pull-right").first().text();

            TeacherFromSSTU t = new TeacherFromSSTU();
            t.setIdPage(id);
            t.setOwnPage(owner);
            if(!t.getOwnPage().equals("Расписание занятий")) {
                teacherRepo.saveAndFlush(t);
            }
        }

        return "База преподавателей обновленна";
    }

    @RequestMapping(value = "/fillstudent", method = RequestMethod.GET)
    @ResponseBody
    public String fillDataStudent() throws IOException {

        String owner;

        for (int id = 1; id < 657; id++) {
            Document dc = Jsoup.connect("http://rasp.sstu.ru/group/" + id).timeout(6000).get();
            owner = dc.select("div.pull-right").first().text();
            if(owner.contains("сессии")){
                owner = owner.substring(27);
            } else {
                owner = owner.substring(19);
            }

            GroupFromSSTU g = new GroupFromSSTU();
            g.setIdPage(id);
            if(!owner.trim().equals("")) {
                g.setOwnPage(owner.trim());
                groupRepo.saveAndFlush(g);
            }
        }
        return "База групп обновленна";
    }
}
