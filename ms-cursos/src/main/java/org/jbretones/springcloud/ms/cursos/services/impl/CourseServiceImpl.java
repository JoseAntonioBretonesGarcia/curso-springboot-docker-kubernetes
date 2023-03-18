package org.jbretones.springcloud.ms.cursos.services.impl;

import org.jbretones.springcloud.ms.cursos.clients.UserClientRest;
import org.jbretones.springcloud.ms.cursos.models.User;
import org.jbretones.springcloud.ms.cursos.models.entity.Course;
import org.jbretones.springcloud.ms.cursos.models.entity.CourseUser;
import org.jbretones.springcloud.ms.cursos.repositories.CourseRepository;
import org.jbretones.springcloud.ms.cursos.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserClientRest userClientRest;


    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return (List<Course>) this.courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return this.courseRepository.findById(id);
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return this.courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<User> assignUserToCourse(User user, Long idCourse) {

        Optional<Course> courseOptional = courseRepository.findById(idCourse);

        if(courseOptional.isPresent()){
            User userMs = userClientRest.getUser(user.getId());
            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMs.getId());
            course.addCourseUsers(courseUser);
            courseRepository.save(course);
            return Optional.of(userMs);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> createAndAssignUserToCourse(User user, Long idCourse) {
        Optional<Course> courseOptional = courseRepository.findById(idCourse);

        if(courseOptional.isPresent()){
            User newUserMs = userClientRest.newUser(user);
            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(newUserMs.getId());
            course.addCourseUsers(courseUser);
            courseRepository.save(course);
            return Optional.of(newUserMs);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> unassignUserOfCourse(User user, Long idCourse) {
        Optional<Course> courseOptional = courseRepository.findById(idCourse);

        if(courseOptional.isPresent()){
            User userMs = userClientRest.getUser(user.getId());
            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMs.getId());
            course.removeCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(userMs);
        }

        return Optional.empty();
    }
}
