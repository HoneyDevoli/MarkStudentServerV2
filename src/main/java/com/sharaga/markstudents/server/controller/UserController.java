package com.sharaga.markstudents.server.controller;

import com.sharaga.markstudents.server.dto.*;
import com.sharaga.markstudents.server.entity.Lesson;
import com.sharaga.markstudents.server.entity.Student;
import com.sharaga.markstudents.server.entity.Subject;
import com.sharaga.markstudents.server.entity.Teacher;
import com.sharaga.markstudents.server.repository.LessonRepository;
import com.sharaga.markstudents.server.repository.StudentRepository;
import com.sharaga.markstudents.server.repository.SubjectRepository;
import com.sharaga.markstudents.server.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    TeacherService teacherService;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    StudentRepository studentRepository;

    @RequestMapping(value = "/addlesson", method = RequestMethod.GET)
    public long addLesson(long idTeacher,String aud, String date,
                          int numLesson, String title,String type, String createOrCheck){

        Lesson newLesson = new Lesson();
        newLesson.setAuditorium(aud);
        newLesson.setDate(date);
        newLesson.setNumberOfLesson(numLesson);

        Teacher teacher = teacherService.getByID(idTeacher);
        Set<Subject> subjects = teacher.getSubjects();
        for(Subject s : subjects){
            if(s.getTitle().equals(title) && s.getType().equals(type)){
                for (Lesson currentLesson : s.getLessons()){
                    if(currentLesson.equals(newLesson)){
                        return currentLesson.getId();
                    }
                }
                if(createOrCheck.equals("Create")) {
                    Subject subject = subjectRepository.getById(s.getId());
                    subject.addLesson(newLesson);
                    subjectRepository.saveAndFlush(subject);

                    subject = subjectRepository.getById(s.getId());
                    for (Lesson currentLesson : subject.getLessons()) {
                        if (currentLesson.equals(newLesson)) {
                            return currentLesson.getId();
                        }
                    }
                }
            }
        }
        return 0;
    }

    @RequestMapping(value = "/getstudents", method = RequestMethod.GET)
    public ArrayStudentDTO getStudents(long idLesson){

        Lesson lesson = lessonRepository.getById(idLesson);

        ArrayStudentDTO students = new ArrayStudentDTO();
        students.setStudents(lesson.getStudents());
        return students;
    }

    @RequestMapping(value = "/markstudent", method = RequestMethod.GET)
    @ResponseBody
    public String markStudent(long idLesson, long idStudent){

        Lesson lesson = lessonRepository.getById(idLesson);

        for(Student currentStudent : lesson.getStudents()){
            if(currentStudent.getId() == idStudent){
                return "Вы уже отметились на этой паре";
            }
        }

        Student student = studentRepository.getById(idStudent);
        lesson.addStudent(student);
        lessonRepository.saveAndFlush(lesson);
        return "Вы успешно отметились";
    }

    @RequestMapping(value = "getstatistics", method = RequestMethod.GET)
    public ArrayStatsDTO getStatistics(long idTeacher){

        Teacher teacher = teacherService.getByID(idTeacher);
        List<StatisticsDTO> stats = new ArrayList<>();

        Set<Subject> subjects = teacher.getSubjects();
        for(Subject currentSubject : subjects){

            StatisticsDTO stat = new StatisticsDTO();
            stat.setSubject(currentSubject);

            List<Lesson> lessons = currentSubject.getLessons();

            stat.setTotalLesson(lessons.size());

            Set<Student> students = new HashSet<>();
            List<Integer> countLessonOfStudent = new ArrayList<>();
            for (Lesson currentLesson : lessons) {

                for (Student currentStudent : currentLesson.getStudents()) {
                    if (students.add(currentStudent)) {
                        int countLesson = 0;
                        for (Lesson lessStud : currentStudent.getLessons()) {
                            if (lessStud.getId() == currentLesson.getId()) {
                                countLesson++;
                            }
                        }
                        countLessonOfStudent.add(countLesson);
                    }
                }
                stat.setStudents(students);
                stat.setCountLesson(countLessonOfStudent);
            }
            stats.add(stat);

        }

        ArrayStatsDTO arrayStats = new ArrayStatsDTO();
        arrayStats.setStats(stats);

        return arrayStats;
    }

    @RequestMapping(value = "/last10mark", method = RequestMethod.GET)
    public ArrayLessonDTO lastMark(long idStudent) {

        Student student = studentRepository.getById(idStudent);
        List<Lesson> lessons = student.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            lessons.get(i).setAuditorium(lessons.get(i).getSubject().getTitle());
        }

        ArrayLessonDTO arrayLesson = new ArrayLessonDTO();
        if(lessons.size()>10) {
            arrayLesson.setLessons(lessons.subList(lessons.size()-10,lessons.size()));
        } else{
            arrayLesson.setLessons(lessons);
        }
        return arrayLesson;
    }


}
