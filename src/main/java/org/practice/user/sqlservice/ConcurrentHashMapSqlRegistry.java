package org.practice.user.sqlservice;

import org.assertj.core.data.MapEntry;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry{
    private Map<String, String> sqlmap = new ConcurrentHashMap<>();

    @Override
    public void registerSql(String key, String sql) {
        sqlmap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlmap.get(key);
        if(!StringUtils.hasText(sql)){
            throw new SqlNotFoundException(key + "를 이용해서 sql을 찾을 수 없습니다.");
        }

        return sql;
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        String oldSql = sqlmap.get(key);
        if(!StringUtils.hasText(oldSql)){
            throw new SqlUpdateFailureException(key + "를 이용해서 sql을 찾을 수 없습니다.");
        }

        sqlmap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for(Map.Entry<String, String> entry : sqlmap.entrySet()) {
            this.updateSql(entry.getKey(), entry.getValue());
        }
    }
}
