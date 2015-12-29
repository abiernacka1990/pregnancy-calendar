package com.miquido.pregnancycalendar.db.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.miquido.pregnancycalendar.db.WeightRepository;
import com.miquido.pregnancycalendar.model.Weight;

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
            DatabaseManager dbManager = new DatabaseManager();
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
            return weights.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
