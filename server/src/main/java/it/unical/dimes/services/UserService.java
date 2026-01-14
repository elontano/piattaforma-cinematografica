package it.unical.dimes.services;

import it.unical.dimes.entities.User;
import it.unical.dimes.exception.UserNotFoundException;
import it.unical.dimes.repositories.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user==null)
            throw new UserNotFoundException(username+" not found!");
        return user;
    }

    public User save (User user){
        return userRepository.save(user);
    }
}
