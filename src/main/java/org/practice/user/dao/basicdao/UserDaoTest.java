package org.practice.user.dao.basicdao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.practice.user.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("id1", "name1", "password1");
        this.user2 = new User("id2", "name2", "password2");
        this.user3 = new User("id3", "name3", "password3");
    }
    
    @Test
    public void addAndGet() throws SQLException {
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
        userDao.deleteAll();
        assertThat(userDao.getCount())
                .isEqualTo(0);

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
        userDao.deleteAll();
        assertThat(userDao.getCount())
                .isEqualTo(0);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.getById("unknownId"));
    }
}
