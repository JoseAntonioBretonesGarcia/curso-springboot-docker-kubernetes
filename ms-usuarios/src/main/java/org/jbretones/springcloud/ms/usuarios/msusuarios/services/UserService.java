package org.jbretones.springcloud.ms.usuarios.msusuarios.services;


import org.jbretones.springcloud.ms.usuarios.msusuarios.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);

    Optional<User> findByEmail(String email);

    List<User> getUsersForIds(List<Long> ids);
}
