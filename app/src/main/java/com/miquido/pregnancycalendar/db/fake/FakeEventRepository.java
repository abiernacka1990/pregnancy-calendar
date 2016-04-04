package com.miquido.pregnancycalendar.db.fake;

import android.support.annotation.Nullable;

import com.miquido.pregnancycalendar.db.EventsRepository;
import com.miquido.pregnancycalendar.model.Event;

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
}
