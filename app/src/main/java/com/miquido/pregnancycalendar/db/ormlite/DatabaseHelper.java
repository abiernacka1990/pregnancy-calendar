package com.miquido.pregnancycalendar.db.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.miquido.pregnancycalendar.model.DiaryEntry;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.model.Weight;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "pregnancyCalendar.db";
    private static final int DATABASE_VERSION = 4;

    private Dao<Weight, Integer> weights = null;
    private Dao<Event, Integer> events = null;
    private Dao<DiaryEntry, Integer> diaryEntries = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Weight.class);
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, DiaryEntry.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Weight.class, true);
            TableUtils.dropTable(connectionSource, Event.class, true);
            TableUtils.dropTable(connectionSource, DiaryEntry.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Weight, Integer> getWeightInfoDao() throws SQLException {
        if (weights == null) {
            weights = getDao(Weight.class);
        }
        return weights;
    }

    public Dao<Event, Integer> getEventsDao() throws SQLException {
        if (events == null) {
            events = getDao(Event.class);
        }
        return events;
    }

    public Dao<DiaryEntry, Integer> getDiaryEntriesDao() throws SQLException {
        if (diaryEntries == null) {
            diaryEntries = getDao(DiaryEntry.class);
        }
        return diaryEntries;
    }

    @Override
    public void close() {
        super.close();
        weights = null;
        events = null;
    }
}
