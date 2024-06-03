package org.practice.config;

import org.practice.user.dao.basicdao.UserDao;
import org.practice.user.service.DummyMailSender;
import org.practice.user.service.UserService;
import org.practice.user.service.UserServiceTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;

@Profile("test")
@Configuration
public class TestAppContext {
    @Bean
    public UserService testUserService(UserDao userDao, MailSender mailSender) {
        UserServiceTest.TestUserServiceImpl testUserService = new UserServiceTest.TestUserServiceImpl();
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        return testUserService;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }
}
