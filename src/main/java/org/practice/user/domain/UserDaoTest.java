package org.practice.user.domain;

import org.practice.user.dao.DaoFactory;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new DaoFactory().userDao();

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
