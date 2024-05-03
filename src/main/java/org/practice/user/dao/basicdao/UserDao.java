package org.practice.user.dao.basicdao;

import org.practice.user.dao.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);
    User getById(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
