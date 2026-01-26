package it.unical.dimes.services;

import it.unical.dimes.entities.User;
import it.unical.dimes.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User registerUser(User user){
        //|| user.getPassword().length() < 8
        if (user.getPassword() == null ) {
            throw new IllegalArgumentException("La password deve essere di almeno 8 caratteri");
        }

        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        return userRepository.save(new User(user.getUsername(),hashed));
    }

    public boolean checkPassword(String candidatePassword, String storedHash) {
        if (storedHash == null || candidatePassword == null)
            return false;
        return BCrypt.checkpw(candidatePassword, storedHash);
    }
}
