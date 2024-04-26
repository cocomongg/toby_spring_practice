package org.practice.user.dao.basicdao;

import org.junit.jupiter.api.Test;
import org.practice.user.dao.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    
    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("firstUserId");
        user.setName("first");
        user.setPassword("firstUserPassword");

        userDao.addUser(user);
        User getUser = userDao.getById(user.getId());

        assertThat(getUser.getName().equals(user.getName()));
        assertThat(getUser.getId().equals(user.getId()));
    }
}
