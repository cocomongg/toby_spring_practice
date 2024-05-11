package org.practice.user.service;

import org.practice.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
