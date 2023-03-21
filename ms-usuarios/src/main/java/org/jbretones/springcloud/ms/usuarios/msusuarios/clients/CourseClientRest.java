package org.jbretones.springcloud.ms.usuarios.msusuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-cursos" , url = "localhost:8002")
public interface CourseClientRest {

    @DeleteMapping("/eliminar-curso-usuario/{userId}")
    void deleteCourseUserByIdUser(@PathVariable Long userId);

}
