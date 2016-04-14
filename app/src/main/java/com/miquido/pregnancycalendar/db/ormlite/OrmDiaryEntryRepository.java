package com.miquido.pregnancycalendar.db.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.miquido.pregnancycalendar.db.DiaryEntriesRepository;
import com.miquido.pregnancycalendar.model.DiaryEntry;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by agnieszka on 13.04.16.
 */
public class OrmDiaryEntryRepository implements DiaryEntriesRepository {

    private DatabaseHelper db;
    Dao<DiaryEntry, Integer> diaryEntries;

    public OrmDiaryEntryRepository(Context ctx) {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            db = dbManager.getHelper(ctx);
            diaryEntries = db.getDiaryEntriesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int create(DiaryEntry DiaryEntry) {
        try {
            return diaryEntries.create(DiaryEntry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public int update(DiaryEntry DiaryEntry) {
        try {
            return diaryEntries.update(DiaryEntry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(DiaryEntry DiaryEntry) {
        try {
            return diaryEntries.delete(DiaryEntry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<DiaryEntry> getAll() {
        try {
            return diaryEntries.queryBuilder().orderBy(DiaryEntry.DATE_FIELD_NAME, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
