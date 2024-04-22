package org.practice.user.dao;

import org.practice.user.domain.ConnectionMaker;
import org.practice.user.domain.DConnectionMaker;
import org.practice.user.domain.UserDao;

public class DaoFactory {
    public UserDao userDao () {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        return new UserDao(connectionMaker);
    }
}
