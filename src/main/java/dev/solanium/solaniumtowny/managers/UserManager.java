package dev.solanium.solaniumtowny.managers;

import dev.solanium.solaniumtowny.repository.impl.UserRepository;

public class UserManager {

    private final UserRepository userRepository;

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
