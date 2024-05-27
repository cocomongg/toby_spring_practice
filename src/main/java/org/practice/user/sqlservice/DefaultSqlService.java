package org.practice.user.sqlservice;

public class DefaultSqlService extends BaseSqlService{
    public DefaultSqlService() {
        super.setSqlReader(new JaxbXmlSqlReader());
        super.setSqlRegistry(new HashMapSqlRegistry());
    }
}
