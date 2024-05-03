package org.practice.user.dao.basicdao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.practice.user.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
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
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("id1", "name1", "password1");
        this.user2 = new User("id2", "name2", "password2");
        this.user3 = new User("id3", "name3", "password3");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dao.deleteAll();
    }
    
    @Test
    public void addAndGet() throws SQLException {
        assertThat(dao.getCount())
                .isEqualTo(0);

        dao.addUser(user1);
        dao.addUser(user2);
        assertThat(dao.getCount())
                .isEqualTo(2);

        User userget1 = dao.getById(user1.getId());
        assertThat(userget1.getName())
                .isEqualTo(user1.getName());
        assertThat(userget1.getId())
                .isEqualTo(user1.getId());

        User userget2 = dao.getById(user2.getId());
        assertThat(userget2.getName())
                .isEqualTo(user2.getName());
        assertThat(userget2.getId())
                .isEqualTo(user2.getId());
    }

    @Test
    public void count() throws SQLException {
        assertThat(dao.getCount())
                .isEqualTo(0);

        dao.addUser(user1);
        assertThat(dao.getCount())
                .isEqualTo(1);

        dao.addUser(user2);
        assertThat(dao.getCount())
                .isEqualTo(2);

        dao.addUser(user3);
        assertThat(dao.getCount())
                .isEqualTo(3);
    }

    @Test
    public void getUserFailure() throws SQLException {
        assertThat(dao.getCount())
                .isEqualTo(0);

        assertThrows(EmptyResultDataAccessException.class, () -> dao.getById("unknownId"));
    }

    @Test
    public void getAll() throws SQLException {
        dao.addUser(user1);
        List<User> users = dao.getAll();
        assertThat(users.size()).isEqualTo(1);
        this.checkSameUser(user1, users.get(0));

        dao.addUser(user2);
        users = dao.getAll();
        assertThat(users.size()).isEqualTo(2);
        this.checkSameUser(user1, users.get(0));
        this.checkSameUser(user2, users.get(1));

        dao.addUser(user3);
        users = dao.getAll();
        assertThat(users.size()).isEqualTo(3);
        this.checkSameUser(user1, users.get(0));
        this.checkSameUser(user2, users.get(1));
        this.checkSameUser(user3, users.get(2));
    }

    @Test
    public void getAllWithNoSavedData() throws SQLException {
        dao.deleteAll();

        List<User> users = dao.getAll();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    public void duplicateKey() {
        dao.deleteAll();

        dao.addUser(user1);
        assertThatThrownBy(() -> dao.addUser(user1)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    public void sqlExceptionTranslate() {
        dao.deleteAll();

        try{
            dao.addUser(user1);
            dao.addUser(user1);
        } catch (DuplicateKeyException ex) {
            SQLException sqlEx = (SQLException) ex.getRootCause();
            SQLErrorCodeSQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlEx)).isInstanceOf(DuplicateKeyException.class);
        }
    }

    private void checkSameUser(User user, User savedUser) {
        assertThat(user.getId()).isEqualTo(savedUser.getId());
        assertThat(user.getName()).isEqualTo(savedUser.getName());
        assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());
    }
}
