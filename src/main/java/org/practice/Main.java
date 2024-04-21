package org.practice;

import org.practice.user.domain.User;
import org.practice.user.domain.UserDao;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();

        User user = new User();
        user.setId("firstUserId");
        user.setName("first");
        user.setPassword("firstUserPassword");

        userDao.addUser(user);
        System.out.println("user 등록 성공");

        User findUser = userDao.getById(user.getId());
        System.out.printf("user 조회 성공 id: %s, name: %s, password: %s%n",
                findUser.getId(), findUser.getName(), findUser.getPassword());

    }
}