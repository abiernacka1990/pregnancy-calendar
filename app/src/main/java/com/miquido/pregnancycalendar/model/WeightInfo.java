package com.miquido.pregnancycalendar.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by agnieszka on 04.11.15.
 */
@DatabaseTable(tableName = "weight_info")
public class WeightInfo {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int week;

    @DatabaseField
    private double weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public static class Builder {

        private WeightInfo weightInfo;

        public Builder() {
            weightInfo = new WeightInfo();
        }

        public Builder setWeek(int week) {
            weightInfo.setWeek(week);
            return this;
        }

        public Builder setWeight(double weight) {
            weightInfo.setWeight(weight);
            return this;
        }

        public WeightInfo build() {
            return weightInfo;
        }
    }
}
