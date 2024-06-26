package org.practice.user.dao.countingdao;

import org.practice.user.domain.User;
import org.practice.user.dao.basicdao.UserDaoJdbc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class CountingDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDaoJdbc userDaoJdbc = context.getBean("userDao", UserDaoJdbc.class);

        User user = new User();
        user.setId("firstUserId");
        user.setName("first");
        user.setPassword("firstUserPassword");

        userDaoJdbc.addUser(user);
        System.out.println("user 등록 성공");

        User findUser = userDaoJdbc.getById(user.getId());
        System.out.printf("user 조회 성공 id: %s, name: %s, password: %s%n",
                findUser.getId(), findUser.getName(), findUser.getPassword());

        CountingConnectionDataSource dataSource = context.getBean("dataSource", CountingConnectionDataSource.class);
        System.out.println("Connection counter: " + dataSource.getCounter());
    }
}
