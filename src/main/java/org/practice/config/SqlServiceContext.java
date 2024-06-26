package org.practice.config;

import org.practice.user.sqlservice.OxmSqlService;
import org.practice.user.sqlservice.SqlService;
import org.practice.user.sqlservice.registry.EmbeddedDbSqlRegistry;
import org.practice.user.sqlservice.registry.SqlRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;

@PropertySource("classpath:/sqlservice.properties")
@Configuration
public class SqlServiceContext {
    @Value("${sql.map.name}")
    private String sqlmapName;

    @Bean
    public SqlService sqlService(Unmarshaller unmarshaller, SqlRegistry sqlRegistry) {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller);
        sqlService.setSqlRegistry(sqlRegistry);
        sqlService.setSqlmap(new ClassPathResource(sqlmapName));

        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry(DataSource embeddedDatabase) {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase);

        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.practice.user.sqlservice.jaxb");

        return marshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:schema.sql")
                .build();
    }
}
