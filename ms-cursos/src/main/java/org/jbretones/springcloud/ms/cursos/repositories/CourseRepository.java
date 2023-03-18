package org.jbretones.springcloud.ms.cursos.repositories;

import org.jbretones.springcloud.ms.cursos.models.entity.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course,Long> {
}
