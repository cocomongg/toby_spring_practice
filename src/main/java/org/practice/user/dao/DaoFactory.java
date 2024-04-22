package org.practice.user.dao;

import org.practice.user.domain.ConnectionMaker;
import org.practice.user.domain.DConnectionMaker;
import org.practice.user.domain.UserDao;

public class DaoFactory {
    public UserDao userDao () {
        return new UserDao(this.connectionMaker());
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
