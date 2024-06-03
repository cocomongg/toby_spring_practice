package org.practice.user.dao.basicdao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.practice.config.TestApplicationContext;
import org.practice.user.domain.Level;
import org.practice.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(classes = TestApplicationContext.class)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("id1", "name1", "password1", "test1@test.com",
                Level.BASIC, 1, 0);
        this.user2 = new User("id2", "name2", "password2", "test2@test.com",
                Level.SILVER, 55, 10);
        this.user3 = new User("id3", "name3", "password3", "test1@test.com",
                Level.GOLD, 100, 40);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        userDao.deleteAll();
    }
    
    @Test
    public void addAndGet() throws SQLException {
        assertThat(userDao.getCount())
                .isEqualTo(0);

        userDao.addUser(user1);
        userDao.addUser(user2);
        assertThat(userDao.getCount())
                .isEqualTo(2);

        User userget1 = userDao.getById(user1.getId());
        checkSameUser(user1, userget1);

        User userget2 = userDao.getById(user2.getId());
        checkSameUser(user2, userget2);
    }

    @Test
    public void count() throws SQLException {
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
        assertThat(userDao.getCount())
                .isEqualTo(0);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.getById("unknownId"));
    }

    @Test
    public void getAll() throws SQLException {
        userDao.addUser(user1);
        List<User> users = userDao.getAll();
        assertThat(users.size()).isEqualTo(1);
        this.checkSameUser(user1, users.get(0));

        userDao.addUser(user2);
        users = userDao.getAll();
        assertThat(users.size()).isEqualTo(2);
        this.checkSameUser(user1, users.get(0));
        this.checkSameUser(user2, users.get(1));

        userDao.addUser(user3);
        users = userDao.getAll();
        assertThat(users.size()).isEqualTo(3);
        this.checkSameUser(user1, users.get(0));
        this.checkSameUser(user2, users.get(1));
        this.checkSameUser(user3, users.get(2));
    }

    @Test
    public void getAllWithNoSavedData() throws SQLException {
        userDao.deleteAll();

        List<User> users = userDao.getAll();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void duplicateKey() {
        userDao.deleteAll();

        userDao.addUser(user1);
        assertThatThrownBy(() -> userDao.addUser(user1)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    public void sqlExceptionTranslate() {
        userDao.deleteAll();

        try{
            userDao.addUser(user1);
            userDao.addUser(user1);
        } catch (DuplicateKeyException ex) {
            SQLException sqlEx = (SQLException) ex.getRootCause();
            SQLErrorCodeSQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlEx)).isInstanceOf(DuplicateKeyException.class);
        }
    }

    @Test
    public void update() {
        userDao.deleteAll();

        userDao.addUser(user1);
        userDao.addUser(user2);

        user1.setName("update-test");
        user1.setPassword("update-password");
        user1.setLevel(Level.GOLD);
        user1.setLogin(100);
        user1.setRecommend(999);
        userDao.update(user1);

        User getUser1 = userDao.getById(user1.getId());
        checkSameUser(user1, getUser1);

        User getUser2 = userDao.getById(user2.getId());
        checkSameUser(user2, getUser2);
    }

    private void checkSameUser(User user, User savedUser) {
        assertThat(user.getId()).isEqualTo(savedUser.getId());
        assertThat(user.getName()).isEqualTo(savedUser.getName());
        assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());
        assertThat(user.getLevel()).isEqualTo(savedUser.getLevel());
        assertThat(user.getLogin()).isEqualTo(savedUser.getLogin());
        assertThat(user.getRecommend()).isEqualTo(savedUser.getRecommend());
    }
}
