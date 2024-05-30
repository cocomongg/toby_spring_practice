package org.practice.config;

import com.mysql.cj.jdbc.Driver;
import org.practice.user.dao.basicdao.UserDao;
import org.practice.user.dao.basicdao.UserDaoJdbc;
import org.practice.user.service.DummyMailSender;
import org.practice.user.service.UserService;
import org.practice.user.service.UserServiceImpl;
import org.practice.user.sqlservice.OxmSqlService;
import org.practice.user.sqlservice.SqlService;
import org.practice.user.sqlservice.registry.EmbeddedDbSqlRegistry;
import org.practice.user.sqlservice.registry.SqlRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.practice.user.service.UserServiceTest.TestUserServiceImpl;

@EnableTransactionManagement
@Configuration
public class TestApplicationContext {
//    @Autowired
//    private SqlService sqlService;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/testdb");
        dataSource.setUsername("root");
        dataSource.setPassword("pcs8437!");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public UserDao userDao(DataSource dataSource, SqlService sqlService) {
        UserDaoJdbc dao = new UserDaoJdbc();
        dao.setDataSource(dataSource);
        dao.setSqlService(sqlService);

        return dao;
    }

    @Bean
    public UserService userService(UserDao userDao, MailSender mailSender) {
        UserServiceImpl service = new UserServiceImpl();
        service.setUserDao(userDao);
        service.setMailSender(mailSender);

        return service;
    }

    @Bean
    public UserService testUserService(UserDao userDao, MailSender mailSender) {
        TestUserServiceImpl testUserService = new TestUserServiceImpl();
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        return testUserService;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public SqlService sqlService(Unmarshaller unmarshaller, SqlRegistry sqlRegistry) {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller);
        sqlService.setSqlRegistry(sqlRegistry);

        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry(DataSource embeddedDatabase) {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase);

        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.practice.user.sqlservice.jaxb");

        return marshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:schema.sql")
                .build();
    }
}
