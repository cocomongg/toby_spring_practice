package org.practice.user.dao;

import org.practice.user.domain.ConnectionMaker;
import org.practice.user.domain.DConnectionMaker;
import org.practice.user.domain.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao () {
        return new UserDao(this.connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
