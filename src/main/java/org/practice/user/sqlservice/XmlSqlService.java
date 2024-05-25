package org.practice.user.sqlservice;

import org.practice.user.sqlservice.jaxb.SqlType;
import org.practice.user.sqlservice.jaxb.Sqlmap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService{
    private Map<String, String> sqlMap = new HashMap<>();

    private String sqlmapFile;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    @PostConstruct
    public void loadSql() {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getXmlFile(this.sqlmapFile));

            for(SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);

        if(!StringUtils.hasText(sql)) {
            throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }

    private File getXmlFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}
