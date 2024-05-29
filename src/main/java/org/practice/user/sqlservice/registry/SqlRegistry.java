package org.practice.user.sqlservice.registry;

import org.practice.user.sqlservice.exception.SqlNotFoundException;

public interface SqlRegistry {
    void registerSql(String key, String sql);

    String findSql(String key) throws SqlNotFoundException;
}
