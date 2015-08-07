/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wadp.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wadp.domain.Course;
import wadp.domain.CourseProgressTracker;
import wadp.domain.Grade;
import wadp.domain.User;
import wadp.repository.CourseProgressRepository;
import wadp.repository.GradeRepository;


/**
 *
 * @author Blackstorm
 */



@Service
public class GradeService {
    
    @Autowired
    GradeRepository gradeRepository;
    
    @Autowired
    CourseProgressRepository courseProgressRepository;
    
    @Transactional
    public void giveGrade(User user, Course course, String grade){
        Grade newGrade = new Grade();
        newGrade.setUser(user);
        newGrade.setCourseId(course.getId());
        newGrade.setCourseName(course.getName());
        newGrade.setGrade(grade);
        gradeRepository.save(newGrade);
        courseProgressRepository.findByUserAndCourse(user, course).get(0).setCompleted(true);
        
    }
    
    public List<Grade> getCourseGrades(Course course){
        return gradeRepository.findByCourseId(course.getId());
    }
    public Grade getStudentCourseGrade(User user, Course course){
        return gradeRepository.findByUserAndCourseId(user, course.getId());
    }
    public List<Grade> getStudentGrades(User user){
        return gradeRepository.findByUser(user);
    }
    
    @Transactional
    public void editGrade(User user, Course course, String grade){
        Grade editable = gradeRepository.findByUserAndCourseId(user, course.getId());
        editable.setGrade(grade);
        gradeRepository.save(editable);
    }
    
}
