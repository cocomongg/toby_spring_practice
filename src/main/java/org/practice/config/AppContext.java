package org.practice.config;

import com.mysql.cj.jdbc.Driver;
import org.practice.user.dao.basicdao.UserDao;
import org.practice.user.service.DummyMailSender;
import org.practice.user.service.UserService;
import org.practice.user.service.UserServiceTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@PropertySource("classpath:/database.properties")
@ComponentScan(basePackages ="org.practice.user")
@EnableSqlService
@EnableTransactionManagement
@Configuration
public class AppContext {
    @Value("${db.driverClass}")
    private Class<? extends Driver> driverClass;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPasswd;

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(this.driverClass);
        dataSource.setUrl(this.dbUrl);
        dataSource.setUsername(this.dbUsername);
        dataSource.setPassword(this.dbPasswd);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Profile("prod")
    @Configuration
    public static class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            return mailSender;
        }
    }

    @Profile("test")
    @Configuration
    public static class TestAppContext {
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
}
