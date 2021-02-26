package com.revature;

import com.revature.models.User;
import com.revature.repositories.UserRepository;
import com.revature.services.UserService;

import java.util.Optional;

public class Driver {


    public static void main(String[] args) {

        User eric;

        eric = new User();
        eric.setEmail("erics@email");
        eric.setFirstname("Eric");
        eric.setLastname("Newman");
        eric.setUsername("enewman");
        eric.setUserId(1);
        eric.setUserRole(1);
        eric.setPassword("Packers1");

        UserRepository userRepository = new UserRepository();

        userRepository.addUser(eric);

        Optional<User> user = userRepository.getAUserByUsernameAndPassword(eric.getUsername(), eric.getPassword());

        System.out.println(user);

    }
}
