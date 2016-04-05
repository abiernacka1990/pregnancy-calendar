package com.miquido.pregnancycalendar.db.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.model.Weight;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /**
     * name of the database file for application
     */
    private static final String DATABASE_NAME = "pregnancyCalendar.db";
    /**
     * database version
     */
    private static final int DATABASE_VERSION = 2;

    /**
     * the DAO object we use to access weight
     */
    private Dao<Weight, Integer> weights = null;
    /**
     * the DAO object we use to access event
     */
    private Dao<Event, Integer> events = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Weight.class);
            TableUtils.createTable(connectionSource, Event.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Weight.class, true);
            TableUtils.dropTable(connectionSource, Event.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the DAO for weights
     * value.
     */
    public Dao<Weight, Integer> getWeightInfoDao() throws SQLException {
        if (weights == null) {
            weights = getDao(Weight.class);
        }
        return weights;
    }

    /**
     * Returns the DAO for events
     * value.
     */
    public Dao<Event, Integer> getEventsDao() throws SQLException {
        if (events == null) {
            events = getDao(Event.class);
        }
        return events;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        weights = null;
        events = null;
    }

}
