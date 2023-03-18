package org.jbretones.springcloud.ms.cursos.services;

import org.jbretones.springcloud.ms.cursos.models.User;
import org.jbretones.springcloud.ms.cursos.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> findAll();

    Optional<Course> findById(Long id);

    Course save(Course course);

    void deleteById(Long id);

    Optional<User> assignUserToCourse ( User user , Long idCourse);

    Optional<User> createAndAssignUserToCourse ( User user , Long idCourse);

    Optional<User> unassignUserOfCourse (User user , Long idCourse);
}
