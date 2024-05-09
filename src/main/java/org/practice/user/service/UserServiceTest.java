package org.practice.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.practice.user.dao.basicdao.UserDao;
import org.practice.user.domain.Level;
import org.practice.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.practice.user.domain.User.MIN_LOGCOUNT_FOR_SILVER;
import static org.practice.user.domain.User.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User> users;

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        public void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("test1", "test1", "p1", "test1@test.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("test2", "test2", "p2", "test2@test.com", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("test3", "test3", "p3", "test3@test.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("test4", "test4", "p4", "test4@test.com", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("test5", "test5", "p5", "test5@test.com", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels () throws Exception {
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

    @Test
    public void upgradeAllOrNothing() throws Exception {
        userDao.deleteAll();
        for(User user : users) {
            userDao.addUser(user);
        }

        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(this.transactionManager);
        testUserService.setMailSender(this.mailSender);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {

        }

        checkLevelUpgraded(users.get(1), false);
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
