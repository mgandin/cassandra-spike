package fr.mga.spike.booking;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryConfiguration.class })
@TestExecutionListeners({ CassandraUnitTestExecutionListener.class })
@EmbeddedCassandra(configuration = "cassandra.yaml", port = 9042)
@CassandraDataSet(value = { "bookingstep.cql" }, keyspace = "booking")
public class BookingStepRepositoryTest {

    private BookingStepRepository bookingStepRepository;

    @Before
    public void setUp() throws Exception {
        Cluster cluster = Cluster.builder().addContactPoints("localhost").build();
        Session session = cluster.connect("booking");
        CassandraTemplate cassandraTemplate = new CassandraTemplate(session);
        bookingStepRepository = new BookingStepRepository(cassandraTemplate);
    }

    @Test
    public void should_do_some_operations() {
        BookingStep bookingStepToInsert = new BookingStep("2", "Book", "VP");
        bookingStepRepository.insert(bookingStepToInsert);
        BookingStep bookingStepInserted = bookingStepRepository.findById("2");
        Assertions.assertThat(bookingStepInserted).isEqualTo(bookingStepToInsert);
    }
}
