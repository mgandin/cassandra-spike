package fr.mga.spike.booking;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.Select;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryConfiguration.class })
@TestExecutionListeners({ CassandraUnitTestExecutionListener.class})
@EmbeddedCassandra(configuration = "cassandra.yaml", port = 9042)
@CassandraDataSet(value = {"bookingstep.cql"}, keyspace = "booking")
public class BookingStepTest {

    private Cluster cluster;
    private Session session;

    @Before
    public void setUp() throws Exception {
        cluster = Cluster.builder().addContactPoints("localhost").build();
        session = cluster.connect("booking");
    }

    @Test
    public void should_do_some_queries() {
        CassandraTemplate cassandraTemplate = new CassandraTemplate(session);
        BookingStep stepToInsert = new BookingStep("1", "Pricing", "BDV");
        cassandraTemplate.insert(stepToInsert);

        Select select = select().from("bookingstep");
        select.where(eq("id", "1"));

        BookingStep stepsInserted = cassandraTemplate.queryForObject(select, new RowMapper<BookingStep>() {
            @Override public BookingStep mapRow(Row row, int i) throws DriverException {
                return new BookingStep(row.getString("id"), row.getString("client"), row.getString("step"));
            }
        });
        Assertions.assertThat(stepsInserted).isEqualTo(stepToInsert);

    }
}
