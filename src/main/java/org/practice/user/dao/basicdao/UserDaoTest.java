package org.practice.user.dao.basicdao;

import org.junit.jupiter.api.Test;
import org.practice.user.dao.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoTest {
    
    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);
        User user1 = new User("id1", "name1", "password1");
        User user2 = new User("id2", "name2", "password2");

        userDao.deleteAll();
        assertThat(userDao.getCount())
                .isEqualTo(0);

        userDao.addUser(user1);
        userDao.addUser(user2);
        assertThat(userDao.getCount())
                .isEqualTo(2);

        User userget1 = userDao.getById(user1.getId());
        assertThat(userget1.getName())
                .isEqualTo(user1.getName());
        assertThat(userget1.getId())
                .isEqualTo(user1.getId());

        User userget2 = userDao.getById(user2.getId());
        assertThat(userget2.getName())
                .isEqualTo(user2.getName());
        assertThat(userget2.getId())
                .isEqualTo(user2.getId());
    }

    @Test
    public void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        userDao.deleteAll();
        assertThat(userDao.getCount())
                .isEqualTo(0);

        User user1 = new User("id1", "name1", "password1");
        User user2 = new User("id2", "name2", "password2");
        User user3 = new User("id3", "name3", "password3");

        userDao.addUser(user1);
        assertThat(userDao.getCount())
                .isEqualTo(1);

        userDao.addUser(user2);
        assertThat(userDao.getCount())
                .isEqualTo(2);

        userDao.addUser(user3);
        assertThat(userDao.getCount())
                .isEqualTo(3);
    }

    @Test
    public void getUserFailure() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        userDao.deleteAll();
        assertThat(userDao.getCount())
                .isEqualTo(0);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.getById("unknownId"));
    }
}
