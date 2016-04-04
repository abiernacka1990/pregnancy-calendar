package com.miquido.pregnancycalendar.db;

import android.support.annotation.Nullable;

import com.miquido.pregnancycalendar.model.Event;

import java.util.List;

/**
 * Created by agnieszka on 10.01.16.
 */
public interface EventsRepository {
    int create(Event event);

    int update(Event event);

    int delete(Event event);

    @Nullable
    List<Event> getAll();

    public Event get(int id);
}
