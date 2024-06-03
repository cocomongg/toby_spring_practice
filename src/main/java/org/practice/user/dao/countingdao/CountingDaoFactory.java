package org.practice.user.dao.countingdao;

import org.practice.user.dao.basicdao.UserDao;
import org.practice.user.dao.basicdao.UserDaoJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class CountingDaoFactory {
    @Bean
    public UserDao userCountingDao () {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        userDaoJdbc.setDataSource(countingConnectionDataSource());
        return userDaoJdbc;
    }

    @Bean
    public DataSource realDataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/springboot");
        dataSource.setUsername("root");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    public DataSource countingConnectionDataSource() {
        return new CountingConnectionDataSource(this.realDataSource());
    }
}
