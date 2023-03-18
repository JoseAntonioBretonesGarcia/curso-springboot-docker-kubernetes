package org.jbretones.springcloud.ms.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.jbretones.springcloud.ms.cursos.models.User;
import org.jbretones.springcloud.ms.cursos.models.entity.Course;
import org.jbretones.springcloud.ms.cursos.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    public ResponseEntity<?> getCourses() {
        List<Course> courses = this.courseService.findAll();
        if (courses.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(courses);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
        Optional<Course> course = this.courseService.findById(id);
        return course.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(course) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/")
    public ResponseEntity<?> newCourse(@Valid @RequestBody Course course, BindingResult result) {
        if(result.hasErrors()){
            return validate(result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.save(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCourse(@Valid @RequestBody Course course, BindingResult result, @PathVariable Long id) {
        if(result.hasErrors()){
            return validate(result);
        }

        Optional<Course> editCourse = this.courseService.findById(id);
        if (editCourse.isPresent()) {
            editCourse.get().setCourse(course);
            return ResponseEntity.status(HttpStatus.OK).body(this.courseService.save(editCourse.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        Optional<Course> courseDelete = this.courseService.findById(id);
        if (courseDelete.isPresent()) {
            this.courseService.deleteById(courseDelete.get().getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> assignUserToCourse(@RequestBody User userParam, @PathVariable Long cursoId){
        Optional<User> user;
        try{
            user = this.courseService.assignUserToCourse(userParam, cursoId);
        }catch (FeignException feignException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "No existe ningún usuario con ese id: " +
                    feignException.getMessage()));
        };
        if(user.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(user.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> newUserToCourse(@RequestBody User userParam, @PathVariable Long cursoId){
        Optional<User> user;
        try{
            user = this.courseService.createAndAssignUserToCourse(userParam, cursoId);
        }catch (FeignException feignException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "No se pudo crear el usuario: " +
                    feignException.getMessage()));
        };
        if(user.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(user.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error ->{
            errors.put(error.getField(),"El campo "+error.getField()+" "+error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
