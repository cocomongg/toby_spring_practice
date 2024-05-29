package org.practice.user.sqlservice;

import org.practice.user.sqlservice.reader.JaxbXmlSqlReader;
import org.practice.user.sqlservice.registry.HashMapSqlRegistry;

public class DefaultSqlService extends BaseSqlService{
    public DefaultSqlService() {
        super.setSqlReader(new JaxbXmlSqlReader());
        super.setSqlRegistry(new HashMapSqlRegistry());
    }
}
