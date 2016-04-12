package com.miquido.pregnancycalendar.db;

import android.support.annotation.Nullable;

import com.miquido.pregnancycalendar.model.Event;

import java.util.List;

/**
 * Created by agnieszka on 10.01.16.
 */
public interface EventsRepository extends BaseRepository<Event> {

    public Event get(int id);

    List<Event> getAllEventsForSpecifiedDay(long date);
}
