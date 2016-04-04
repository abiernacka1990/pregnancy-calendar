package com.miquido.pregnancycalendar.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by agnieszka on 10.01.16.
 */
@DatabaseTable(tableName = "events")
public class Event {
    public static final String TITLE_FIELD_NAME = "title";
    public static final String DATE_FIELD_NAME = "date";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, columnName = TITLE_FIELD_NAME)
    private String title;

    @DatabaseField(canBeNull = false, columnName = DATE_FIELD_NAME)
    private String date;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static Event getFakeEvent() {
        Event event = new Event();
        event.setTitle("Fake Event");
        event.setDate("2016-01-01");
        return event;
    }
}
