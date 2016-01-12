package com.miquido.pregnancycalendar.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import hirondelle.date4j.DateTime;

/**
 * Created by agnieszka on 10.01.16.
 */
@DatabaseTable(tableName = "events")
public class Event {
    public static final String NAME_FIELD_NAME = "name";
    public static final String DATE_FIELD_NAME = "date";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, columnName = NAME_FIELD_NAME)
    private String name;

    @DatabaseField(canBeNull = false, columnName = DATE_FIELD_NAME)
    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static Event getFakeEvent() {
        Event event = new Event();
        event.setName("Fake Event");
        event.setDate("2016-01-01");
        return event;
    }
}
