package fr.mga.spike.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = { "fr.mga.spike.booking" })
public class RepositoryConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceHolderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceHolderConfigurer.setLocations(new ClassPathResource[] { new ClassPathResource("cassandra.properties") });
        return propertySourcesPlaceHolderConfigurer;
    }

    @Bean
    public CassandraClusterFactoryBean cluster(@Value("${spike.cassandra.contactpoints}") String contactPoints,
            @Value("${spike.cassandra.port}") Integer port) {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(contactPoints);
        cluster.setPort(port);
        return cluster;
    }

    @Bean
    public CassandraSessionFactoryBean session(CassandraClusterFactoryBean cluster, @Value("${spike.cassandra.keyspace}") String keyspace)
            throws Exception {
        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster.getObject());
        session.setKeyspaceName(keyspace);
        session.setConverter(new MappingCassandraConverter(new BasicCassandraMappingContext()));
        session.setSchemaAction(SchemaAction.NONE);
        return session;
    }

    @Bean
    public CassandraTemplate cassandraTemplate(CassandraSessionFactoryBean session) {
        return new CassandraTemplate(session.getObject());
    }

    @Bean
    public BookingStepRepository bookingStepRepository(CassandraTemplate cassandraTemplate) {
        return new BookingStepRepository(cassandraTemplate);
    }
}
