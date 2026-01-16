package it.unical.dimes.services;

import it.unical.dimes.entities.User;
import it.unical.dimes.repositories.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User save (User user){
        return userRepository.save(user);
    }
}
