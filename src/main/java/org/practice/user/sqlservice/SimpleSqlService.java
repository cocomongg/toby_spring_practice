package org.practice.user.sqlservice;

import org.practice.user.sqlservice.exception.SqlRetrievalFailureException;
import org.springframework.util.StringUtils;

import java.util.Map;

public class SimpleSqlService implements SqlService{
    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);

        if(!StringUtils.hasText(sql)) {
            throw new SqlRetrievalFailureException(key + "에 해당하는 sql이 없습니다.");
        }

        return sql;
    }
}
