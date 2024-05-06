package org.practice.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.practice.user.dao.basicdao.UserDao;
import org.practice.user.domain.Level;
import org.practice.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.practice.user.domain.User.MIN_LOGCOUNT_FOR_SILVER;
import static org.practice.user.domain.User.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("test1", "test1", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("test2", "test2", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("test3", "test3", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("test4", "test4", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("test5", "test5", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels () {
        userDao.deleteAll();

        for(User user : users) {
            userDao.addUser(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User getUserWithLevel = userDao.getById(userWithLevel.getId());
        User getUserWithoutLevel = userDao.getById(userWithoutLevel.getId());

        assertThat(getUserWithLevel.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(getUserWithoutLevel.getLevel()).isEqualTo(userWithoutLevel.getLevel());
    }

    private void checkLevel(User user, Level expectedLevel) {
        User getUser = userDao.getById(user.getId());
        assertThat(getUser.getLevel()).isEqualTo(expectedLevel);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.getById(user.getId());
        if(upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().getNext());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }

    }
}
