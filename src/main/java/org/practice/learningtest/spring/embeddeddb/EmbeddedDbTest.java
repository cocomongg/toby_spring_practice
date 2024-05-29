package org.practice.learningtest.spring.embeddeddb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedDbTest {
    EmbeddedDatabase db;
    JdbcTemplate template;

    @BeforeEach
    public void setup() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        template = new JdbcTemplate(db);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void initData() {
        assertThat(template.queryForObject("select count(*) from sqlmap", Integer.class))
                .isEqualTo(2);

        List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_");
        assertThat((String)list.get(0).get("key_")).isEqualTo("KEY1");
        assertThat((String)list.get(0).get("sql_")).isEqualTo("SQL1");
        assertThat((String)list.get(1).get("key_")).isEqualTo("KEY2");
        assertThat((String)list.get(1).get("sql_")).isEqualTo("SQL2");
    }

    @Test
    public void insert() {
        template.update("insert into sqlmap(key_, sql_) values (?, ?)", "KEY3", "SQL3");

        assertThat(template.queryForObject("select count(*) from sqlmap", Integer.class))
                .isEqualTo(3);
    }
}
