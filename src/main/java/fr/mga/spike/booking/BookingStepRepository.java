package fr.mga.spike.booking;

import com.datastax.driver.core.querybuilder.Select;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

@Repository
public class BookingStepRepository {

    private CassandraTemplate cassandraTemplate;

    public BookingStepRepository(CassandraTemplate cassandraTemplate) {
        this.cassandraTemplate = cassandraTemplate;
    }

    public void insert(BookingStep bookingStep) {
        cassandraTemplate.insert(bookingStep);
    }

    public BookingStep findById(String id) {
        Select select = select().from("bookingstep");
        select.where(eq("id", id));

        BookingStep bookingStep = cassandraTemplate.queryForObject(select,
                (row, i) -> new BookingStep(row.getString("id"), row.getString("client"), row.getString("step")));
        return bookingStep;
    }
}
