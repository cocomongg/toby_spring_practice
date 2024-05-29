package org.practice.user.sqlservice;

import org.practice.user.sqlservice.exception.SqlNotFoundException;
import org.practice.user.sqlservice.exception.SqlRetrievalFailureException;
import org.practice.user.sqlservice.reader.SqlReader;
import org.practice.user.sqlservice.registry.SqlRegistry;

import javax.annotation.PostConstruct;

public class BaseSqlService implements SqlService {
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }
}
