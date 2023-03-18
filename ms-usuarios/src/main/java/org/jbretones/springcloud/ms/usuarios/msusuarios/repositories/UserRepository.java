package org.jbretones.springcloud.ms.usuarios.msusuarios.repositories;

import org.jbretones.springcloud.ms.usuarios.msusuarios.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    //this query is same than the method from jpa, I put like example
    //@Query("select u from Usuari where u.email=?1")
    Optional<User> findByEmail(String email);

    //this valid if exist or not an user with these email
    boolean existsByEmail(String email);

}
