package dev.solanium.solaniumtowny.repository.impl;

import com.j256.ormlite.support.ConnectionSource;
import dev.solanium.solaniumtowny.repository.Repository;
import dev.solanium.solaniumtowny.user.User;

import java.util.Comparator;
import java.util.UUID;

public class UserRepository extends Repository<User, UUID> {

    public UserRepository(ConnectionSource connectionSource) {
        super(connectionSource, User.class, Comparator.comparing(User::getUuid));
    }
}
