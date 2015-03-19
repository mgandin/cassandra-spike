package fr.mga.spike.booking;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Objects;

@Table(value = "bookingstep")
public class BookingStep {

    @PrimaryKey
    private String id;

    private String step;

    private String client;

    public BookingStep(String id, String step, String client) {
        this.id = id;
        this.step = step;
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public String getStep() {
        return step;
    }

    public String getClient() {
        return client;
    }

    @Override public String toString() {
        return "BookingStep{" +
                "id='" + id + '\'' +
                ", step='" + step + '\'' +
                ", client='" + client + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookingStep that = (BookingStep) o;
        return Objects.equals(that.getId(),id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
