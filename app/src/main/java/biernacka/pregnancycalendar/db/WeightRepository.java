package biernacka.pregnancycalendar.db;

import biernacka.pregnancycalendar.model.Weight;

/**
 * Created by agnieszka on 29.12.15.
 */
public interface WeightRepository extends BaseRepository<Weight> {

    int create(Weight weight);

    int update(Weight weight);

    int delete(Weight weight);

    boolean exist(int week);

    int updateSpecifiedWeek(Weight weight);

    double getWeightForWeek(int week) throws Exception;
}
