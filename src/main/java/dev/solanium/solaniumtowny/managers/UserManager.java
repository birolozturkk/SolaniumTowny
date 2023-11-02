package dev.solanium.solaniumtowny.managers;

import dev.solanium.solaniumtowny.repository.impl.UserRepository;
import dev.solanium.solaniumtowny.user.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class UserManager {

    private final UserRepository userRepository;

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(OfflinePlayer offlinePlayer) {
        Optional<User> userOptional = getUserByUUID(offlinePlayer.getUniqueId());
        if (userOptional.isPresent()) return userOptional.get();
        User user = new User(offlinePlayer.getUniqueId());
        userRepository.addEntry(user);
        return user;

    }

    public Optional<User> getUserByUUID(UUID uuid) {
        return userRepository.findUserByUUID(uuid);
    }
}
