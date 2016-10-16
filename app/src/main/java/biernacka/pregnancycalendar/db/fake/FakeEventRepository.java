package biernacka.pregnancycalendar.db.fake;

import android.support.annotation.Nullable;

import biernacka.pregnancycalendar.db.EventsRepository;
import biernacka.pregnancycalendar.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agnieszka on 12.01.16.
 */
public class FakeEventRepository implements EventsRepository {

    @Override
    public int create(Event event) {
        return 0;
    }

    @Override
    public int update(Event event) {
        return 0;
    }

    @Override
    public int delete(Event event) {
        return 0;
    }

    @Nullable
    @Override
    public List<Event> getAll() {
        List<Event> list = new ArrayList<>();
        list.add(Event.getFakeEvent());
        return list;
    }

    @Override
    public Event get(int id) {
        return Event.getFakeEvent();
    }

    @Override
    public List<Event> getAllEventsForSpecifiedDay(long date) {
        return null;
    }

    @Override
    public List<Event> findUpcomingEvents(long date, long numberOfEvents) {
        return null;
    }
}
