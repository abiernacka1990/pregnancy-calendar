package com.miquido.pregnancycalendar.db;

import android.support.annotation.Nullable;

import com.miquido.pregnancycalendar.model.Weight;

import java.util.List;

/**
 * Created by agnieszka on 29.12.15.
 */
public interface WeightRepository extends BaseRepository<Weight> {

    int create(Weight weight);

    int update(Weight weight);

    int delete(Weight weight);

    boolean exist(int week);

    int updateSpecifiedWeek(Weight weight);
}
