package com.sharaga.markstudents.server.controller;

import com.sharaga.markstudents.server.dto.*;
import com.sharaga.markstudents.server.entity.*;
import com.sharaga.markstudents.server.repository.CodeRepository;
import com.sharaga.markstudents.server.repository.LessonRepository;
import com.sharaga.markstudents.server.repository.StudentRepository;
import com.sharaga.markstudents.server.repository.SubjectRepository;
import com.sharaga.markstudents.server.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private CodeRepository codeRepository;

    @RequestMapping(value = "/addlesson", method = RequestMethod.GET)
    public long addLesson(long idTeacher,String aud, String date,
                          int numLesson, String title,String type, String createOrCheck){

        Lesson newLesson = new Lesson();
        newLesson.setAuditorium(aud);
        newLesson.setDate(date);
        newLesson.setNumberOfLesson(numLesson);

        Teacher teacher = teacherService.getByID(idTeacher);

        Code newCode = new Code();
        newCode.setMarkOpportunity(true);
        newCode.setTeacher(teacher);

        Set<Subject> subjects = teacher.getSubjects();
        for(Subject s : subjects){
            if(s.getTitle().equals(title) && s.getType().equals(type)){
                for (Lesson currentLesson : s.getLessons()){
                    if(currentLesson.equals(newLesson)){

                        newCode.setCode(currentLesson.getId()+"/"+1);
                        codeRepository.saveAndFlush(newCode);

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

                            newCode = codeRepository.saveAndFlush(newCode);
                            newCode.setCode(currentLesson.getId()+"/"+1);
                            codeRepository.saveAndFlush(newCode);

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
    public String markStudent(String idLesson, long idStudent){

        long idLessonNum = Long.valueOf(idLesson.substring(0,idLesson.indexOf("/")));
        Lesson lesson = lessonRepository.getById(idLessonNum);

        for(Student currentStudent : lesson.getStudents()){
            if(currentStudent.getId() == idStudent){
                return "Вы уже отметились на этой паре";
            }
        }

        List<Code> codes = codeRepository.findAllByCode(idLesson);
        if(codes.size() == 0) return "Фейковый QR-код";
        Code lastCode = codes.get(codes.size()-1);

        if(lastCode.isMarkOpportunity()) {

            Date date = new Date();
            lastCode.setDate(date);
            lastCode.setMarkOpportunity(false);

            Student student = studentRepository.getById(idStudent);
            student.addCode(lastCode);
            lesson.addStudent(student);
            lessonRepository.saveAndFlush(lesson);
            return "Вы успешно отметились";

        } else {
            Code fakeCode = new Code();
            Date date = new Date();
            fakeCode.setDate(date);
            fakeCode.setMarkOpportunity(false);
            fakeCode.setCode(idLesson.replace("/","fake"));
            fakeCode.setTeacher(lastCode.getTeacher());

            Student student = studentRepository.getById(idStudent);
            student.addCode(fakeCode);
            studentRepository.saveAndFlush(student);
            return "Попытка отметится по недействительному коду";
        }
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
                        for (Lesson lessStud : lessons) {
                            for(Student stud: lessStud.getStudents())
                                if (currentStudent.getId() == stud.getId()) {
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

    @RequestMapping(value = "/generatecode")
    public void generateCode(long idTeacher, long idLesson, int numberOfCode, boolean isMarkOpport){

        Teacher teacher = teacherService.getByID(idTeacher);

        if(!isMarkOpport) {
            List<Code> codes = codeRepository.findAllByCode(idLesson + "/" + numberOfCode);
            if (codes.size() != 0) {
                Code lastCode = codes.get(codes.size() - 1);
                lastCode.setMarkOpportunity(false);
                codeRepository.saveAndFlush(lastCode);
            }
        } else {
            Code newCode = new Code();
            newCode.setMarkOpportunity(isMarkOpport);
            newCode.setTeacher(teacher);
            newCode.setCode(idLesson + "/" + numberOfCode);

            codeRepository.saveAndFlush(newCode);
        }
    }

    @RequestMapping(value="/fakemark")
    public ArrayCodeDTO fakeMark(long idTeacher){

        List<Code> fakesMark= new ArrayList<>();
        for(Code currentCode : teacherService.getByID(idTeacher).getCodes() ){
            if(currentCode.getCode().contains("fake")){
                fakesMark.add(currentCode);
            }
        }
        ArrayCodeDTO arrayCodeDTO = new ArrayCodeDTO();
        arrayCodeDTO.setCodes(fakesMark);
        return arrayCodeDTO;
    }

    @RequestMapping(value="/deletemarks")
    public void deleteMarks(long idTeacher){
        Teacher teacher = teacherService.getByID(idTeacher);
        List<Code> codes = codeRepository.findAllByTeacher(teacher);
        for(Code code : codes){
            if(code.getCode().contains("fake")){
                codeRepository.delete(code);
            }
        }
    }
}
