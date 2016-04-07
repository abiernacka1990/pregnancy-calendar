package com.miquido.pregnancycalendar.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.utils.StringFormatter;

import org.joda.time.DateTime;

/**
 * Created by agnieszka on 10.01.16.
 */
@DatabaseTable(tableName = "events")
public class Event {
    public static final String TITLE_FIELD_NAME = "title";
    public static final String START_DATE_FIELD_NAME = "startDate";
    public static final String END_DATE_FIELD_NAME = "endDate";
    public static final String NOTE_FIELD_NAME = "note";
    public static final String ADDRESS_FIELD_NAME = "address";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, columnName = TITLE_FIELD_NAME)
    private String title;

    @DatabaseField(canBeNull = false, columnName = START_DATE_FIELD_NAME)
    private long startDate;

    @DatabaseField(canBeNull = false, columnName = END_DATE_FIELD_NAME)
    private long endDate;

    @DatabaseField(canBeNull = true, columnName = NOTE_FIELD_NAME)
    private String note;

    @DatabaseField(canBeNull = true, columnName = ADDRESS_FIELD_NAME)
    private String address;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubtitle(Context context) {
        StringBuilder subtitleBuilder = new StringBuilder();
        subtitleBuilder.append(StringFormatter.withDayOfWeekDate(getStartDateAsDateTime()));
        subtitleBuilder.append(", ");
        subtitleBuilder.append(StringFormatter.time(getStartDateAsDateTime()));
        if (address != null && !address.isEmpty()) {
            subtitleBuilder.append(" ");
            subtitleBuilder.append(context.getString(R.string.subtitle_in));
            subtitleBuilder.append(" ");
            subtitleBuilder.append(address);
        }
        return subtitleBuilder.toString();
    }

    @NonNull
    private DateTime getStartDateAsDateTime() {
        return new DateTime(startDate);
    }

    public String getEventTitle(Context context) {
       return title == null || title.isEmpty() ? context.getString(R.string.default_event_title) : title;
    }

    public static Event getFakeEvent() {
        Event event = new Event();
        event.setTitle("Fake Event");
        event.setStartDate(new DateTime().getMillis());
        event.setEndDate(new DateTime().getMillis());
        return event;
    }
}
