package org.practice.user.sqlservice.reader;

import org.practice.user.sqlservice.registry.SqlRegistry;

public interface SqlReader {
    void read(SqlRegistry sqlRegistry);
}
