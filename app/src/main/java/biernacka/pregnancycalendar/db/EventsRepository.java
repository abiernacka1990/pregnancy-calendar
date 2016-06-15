package biernacka.pregnancycalendar.db;

import biernacka.pregnancycalendar.model.Event;

import java.util.List;

/**
 * Created by agnieszka on 10.01.16.
 */
public interface EventsRepository extends BaseRepository<Event> {

    Event get(int id);

    List<Event> getAllEventsForSpecifiedDay(long date);
}
