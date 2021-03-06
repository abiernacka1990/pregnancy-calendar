package com.miquido.pregnancycalendar.db.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.miquido.pregnancycalendar.db.EventsRepository;
import com.miquido.pregnancycalendar.model.Event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
 * Created by agnieszka on 10.01.16.
 */
public class OrmEventRepository implements EventsRepository {
    private DatabaseHelper db;
    Dao<Event, Integer> events;

    public OrmEventRepository(Context ctx) {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            db = dbManager.getHelper(ctx);
            events = db.getEventsDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int create(Event event) {
        try {
            return events.create(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public int update(Event event) {
        try {
            return events.update(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Event event) {
        try {
            return events.delete(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Event> getAll() {
        try {
            return events.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<DateTime> getAllDates() {
        List<Event> events = getAll();
        ArrayList<DateTime> dateTimes = new ArrayList<>();
        for (Event event: events) {
            DateTime dateTime = new DateTime(event.getDate());
            dateTimes.add(dateTime);
        }
        return dateTimes;
    }
}
