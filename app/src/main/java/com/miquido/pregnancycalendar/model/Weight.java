package com.miquido.pregnancycalendar.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by agnieszka on 04.11.15.
 */
@DatabaseTable(tableName = "weights")
public class Weight {

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

        private Weight weight;

        public Builder() {
            weight = new Weight();
        }

        public Builder setWeek(int week) {
            weight.setWeek(week);
            return this;
        }

        public Builder setWeight(double weight) {
            this.weight.setWeight(weight);
            return this;
        }

        public Weight build() {
            return weight;
        }
    }
}
