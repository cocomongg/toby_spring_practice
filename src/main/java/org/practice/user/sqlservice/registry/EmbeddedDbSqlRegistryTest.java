package org.practice.user.sqlservice.registry;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.practice.user.sqlservice.exception.SqlUpdateFailureException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{
    EmbeddedDatabase db;

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @Test
    public void transactionalUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("UNKNOWN_KEY", "Modified!!!!");

        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        } catch (SqlUpdateFailureException e) {

        }

        checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
