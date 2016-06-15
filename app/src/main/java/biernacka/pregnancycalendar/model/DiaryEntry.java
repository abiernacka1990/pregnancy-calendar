package biernacka.pregnancycalendar.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by agnieszka on 13.04.16.
 */
@DatabaseTable(tableName = "diary_entries")
public class DiaryEntry {
    public static final String TEXT_FIELD_NAME = "text";
    public static final String DATE_FIELD_NAME = "date";
    public static final String IMAGE_FIELD_NAME = "image";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true, columnName = TEXT_FIELD_NAME)
    private String text;

    @DatabaseField(canBeNull = false, columnName = DATE_FIELD_NAME)
    private long date;

    @DatabaseField(canBeNull = true, columnName = IMAGE_FIELD_NAME)
    private String imagePath;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static class Builder {
        private DiaryEntry diaryEntry;

        public Builder() {
            diaryEntry = new DiaryEntry();
        }

        public Builder setDate(long date) {
            diaryEntry.setDate(date);
            return this;
        }

        public Builder setText(String text) {
            diaryEntry.setText(text);
            return this;
        }
        public Builder setImagePath(String imagePath) {
            diaryEntry.setImagePath(imagePath);
            return this;
        }

        public DiaryEntry build() {
            return diaryEntry;
        }
    }
}
