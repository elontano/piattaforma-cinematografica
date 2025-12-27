package it.unical.dimes.repositories;

import it.unical.dimes.entities.User;

public interface UserRepository {

    User findByUsername(String username);

    User save (User user);
}
