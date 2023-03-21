package org.jbretones.springcloud.ms.usuarios.msusuarios.services.impl;

import org.jbretones.springcloud.ms.usuarios.msusuarios.clients.CourseClientRest;
import org.jbretones.springcloud.ms.usuarios.msusuarios.models.User;
import org.jbretones.springcloud.ms.usuarios.msusuarios.repositories.UserRepository;
import org.jbretones.springcloud.ms.usuarios.msusuarios.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseClientRest courseClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
        this.courseClientRest.deleteCourseUserByIdUser(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersForIds(List<Long> ids) {
        return (List<User>) userRepository.findAllById(ids);
    }


}
