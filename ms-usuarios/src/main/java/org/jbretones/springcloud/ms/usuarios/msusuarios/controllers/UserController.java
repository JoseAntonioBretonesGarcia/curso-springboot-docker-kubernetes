package org.jbretones.springcloud.ms.usuarios.msusuarios.controllers;

import jakarta.validation.Valid;
import org.jbretones.springcloud.ms.usuarios.msusuarios.models.User;
import org.jbretones.springcloud.ms.usuarios.msusuarios.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getUsers() {
        List<User> users = this.userService.findAll();
        if(users.size()>0){
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> userOptional = this.userService.findById(id);
        return userOptional.isPresent() ? ResponseEntity.ok(userOptional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> newUser(@Valid @RequestBody User user, BindingResult result){

        if(this.userService.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese email"));
        }

        if(result.hasErrors()){
            return validate(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return validate(result);
        }
        Optional<User> editUser = this.userService.findById(id);
        if(editUser.isPresent()){
            if(!user.getEmail().equalsIgnoreCase(editUser.get().getEmail())
                    && this.userService.findByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese email"));
            }
            editUser.get().setUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(editUser.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Optional<User> user = this.userService.findById(id);
        if(user.isPresent()){
            this.userService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
