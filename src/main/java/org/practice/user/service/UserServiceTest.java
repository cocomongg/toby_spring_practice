package org.practice.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.practice.user.dao.basicdao.UserDao;
import org.practice.user.domain.Level;
import org.practice.user.domain.User;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.practice.user.domain.User.MIN_LOGCOUNT_FOR_SILVER;
import static org.practice.user.domain.User.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User> users;

    static class TestUserService extends UserServiceImpl {
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

    @DirtiesContext
    @Test
    public void upgradeLevels () throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
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

    @DirtiesContext
    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserServiceImpl testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        ProxyFactoryBean pfBean = context.getBean("&userService", ProxyFactoryBean.class);
        pfBean.setTarget(testUserService);
        UserService userServiceTx = (UserService) pfBean.getObject();

        userDao.deleteAll();
        for(User user : users) {
            userDao.addUser(user);
        }

        try {
            userServiceTx.upgradeLevels();
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

    private void checkUserAndLevel(User updated, String expected, Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expected);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }
}
