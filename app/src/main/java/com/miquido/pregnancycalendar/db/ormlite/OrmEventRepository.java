package com.miquido.pregnancycalendar.db.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.miquido.pregnancycalendar.db.EventsRepository;
import com.miquido.pregnancycalendar.model.Event;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.List;

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
    public Event get(int id) {
        try {
            return events.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Event> getAllEventsForSpecifiedDuration(DateTime startDate, DateTime endDate) {

        try {
            return events
                    .queryBuilder()
                    .where()
                    .ge(Event.START_DATE_FIELD_NAME, startDate.getMillis())
                    .and()
                    .lt(Event.START_DATE_FIELD_NAME, endDate.getMillis())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> getAllEventsForSpecifiedDay(long date) {
        DateTime startOfDay = new DateTime(date).withTime(0, 0, 0, 0);
        DateTime endOfDay = new DateTime(startOfDay).plusDays(1);
        return getAllEventsForSpecifiedDuration(startOfDay, endOfDay);
    }

}
