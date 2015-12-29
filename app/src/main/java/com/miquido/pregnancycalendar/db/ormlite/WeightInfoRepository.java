package com.miquido.pregnancycalendar.db.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.miquido.pregnancycalendar.model.WeightInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by agnieszka on 19.02.15.
 */
public class WeightInfoRepository {
    private DatabaseHelper db;
    Dao<WeightInfo, Integer> weightInfos;

    public WeightInfoRepository(Context ctx) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(ctx);
            weightInfos = db.getWeightInfoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int create(WeightInfo weightInfo) {
        try {
            return weightInfos.create(weightInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(WeightInfo weightInfo) {
        try {
            return weightInfos.update(weightInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(WeightInfo weightInfo) {
        try {
            return weightInfos.delete(weightInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<WeightInfo> getAll() {
        try {
            return weightInfos.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
