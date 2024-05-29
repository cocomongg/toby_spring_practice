package org.practice.user.sqlservice;

import org.practice.user.sqlservice.exception.SqlRetrievalFailureException;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
