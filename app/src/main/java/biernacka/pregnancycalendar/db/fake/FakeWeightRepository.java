package biernacka.pregnancycalendar.db.fake;

import android.support.annotation.Nullable;

import biernacka.pregnancycalendar.db.WeightRepository;
import biernacka.pregnancycalendar.model.Weight;

import java.util.List;

/**
 * Created by agnieszka on 12.01.16.
 */
public class FakeWeightRepository implements WeightRepository {
    @Override
    public int create(Weight weight) {
        return 0;
    }

    @Override
    public int update(Weight weight) {
        return 0;
    }

    @Override
    public int delete(Weight weight) {
        return 0;
    }

    @Nullable
    @Override
    public List<Weight> getAll() {
        return null;
    }

    @Override
    public boolean exist(int week) {
        return false;
    }

    @Override
    public int updateSpecifiedWeek(Weight weight) {
        return 0;
    }
}