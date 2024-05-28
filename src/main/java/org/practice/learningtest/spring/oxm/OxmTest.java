package org.practice.learningtest.spring.oxm;

import org.junit.jupiter.api.Test;
import org.practice.user.sqlservice.jaxb.SqlType;
import org.practice.user.sqlservice.jaxb.Sqlmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(locations = "/oxmTest-context.xml")
@SpringBootTest
public class OxmTest {
    @Autowired
    private Unmarshaller unmarshaller;

    @Test
    public void unmarshallSqlMap() throws IOException {
        Source xmlSource = new StreamSource(this.getXmlFile("sqlmap.xml"));

        Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);

        List<SqlType> sqlList = sqlmap.getSql();
        assertThat(sqlList.size()).isEqualTo(6);
        assertThat(sqlList.get(0).getKey()).isEqualTo("userAdd");
    }

    private File getXmlFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}
