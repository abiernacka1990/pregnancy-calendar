package com.miquido.pregnancycalendar.db;

import com.miquido.pregnancycalendar.model.Weight;

import java.util.List;

/**
 * Created by agnieszka on 29.12.15.
 */
public interface WeightRepository {

    int create(Weight weight);

    int update(Weight weight);

    int delete(Weight weight);

    List<Weight> getAll();

}
