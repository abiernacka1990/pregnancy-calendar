package biernacka.pregnancycalendar.db.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import biernacka.pregnancycalendar.db.WeightRepository;
import biernacka.pregnancycalendar.model.Weight;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by agnieszka on 19.02.15.
 */
public class OrmWeightRepository implements WeightRepository {
    private DatabaseHelper db;
    Dao<Weight, Integer> weights;

    public OrmWeightRepository(Context ctx) {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            db = dbManager.getHelper(ctx);
            weights = db.getWeightInfoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int create(Weight weight) {
        try {
            return weights.create(weight);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateSpecifiedWeek(Weight weight) {
        try {
            UpdateBuilder<Weight, Integer> updateBuilder = weights.updateBuilder();
            updateBuilder.updateColumnValue(Weight.WEIGHT_FIELD_NAME, weight.getWeight());
            updateBuilder.where().eq(Weight.WEEK_FIELD_NAME, weight.getWeek());
            return updateBuilder.update();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Weight weight) {
        try {
            return weights.update(weight);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Weight weight) {
        try {
            return weights.delete(weight);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Weight> getAll() {
        try {
            return weights.queryBuilder().orderBy(Weight.WEEK_FIELD_NAME, true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean exist(int week) {
        try {
            return weights.queryBuilder().where().eq(Weight.WEEK_FIELD_NAME, week).query().size() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public double getWeightForWeek(int week) throws Exception {
        return weights.queryBuilder().where().eq(Weight.WEEK_FIELD_NAME, week).query().get(0).getWeight();
    }
}
