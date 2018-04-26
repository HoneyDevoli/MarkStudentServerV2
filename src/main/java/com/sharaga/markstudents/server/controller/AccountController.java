package com.sharaga.markstudents.server.controller;

import com.sharaga.markstudents.server.entity.Teacher;
import com.sharaga.markstudents.server.repository.GroupFromSSTURepository;
import com.sharaga.markstudents.server.repository.TeacherFromSSTURepository;
import com.sharaga.markstudents.server.repository.TeacherRepository;
import com.sharaga.markstudents.server.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "/loginteacher", method = RequestMethod.GET)
    public Teacher loginTeacher(String login,String password) throws IOException {
        return  teacherService.getTeacherAuthorization(login,password);
    }
}

