package org.jbretones.springcloud.ms.cursos.clients;

import org.jbretones.springcloud.ms.cursos.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-usuarios", url = "localhost:8001")
public interface UserClientRest {

    @GetMapping("/{id}")
    User getUser(@PathVariable Long id);

    @PostMapping("/")
    User newUser(@RequestBody User user);

    @GetMapping("/usuarios-por-curso")
    List<User> getUsersForCourse(@RequestParam List<Long> ids);
}
