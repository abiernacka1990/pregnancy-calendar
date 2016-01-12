package com.miquido.pregnancycalendar.db;

import android.support.annotation.Nullable;

import com.miquido.pregnancycalendar.model.Event;

import java.util.ArrayList;
import java.util.List;

import hirondelle.date4j.DateTime;

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
    public ArrayList<DateTime> getAllDates() {
        ArrayList<DateTime> list = new ArrayList<>();
        list.add(new DateTime("2016-01-01"));
        list.add(new DateTime("2016-01-31"));
        return list;
    }
}
