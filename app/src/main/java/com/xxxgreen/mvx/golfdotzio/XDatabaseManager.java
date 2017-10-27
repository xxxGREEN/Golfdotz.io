package com.xxxgreen.mvx.golfdotzio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_1;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_2;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_3;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_4;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_5;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_6;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_7;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.TABLE_NAME;


/**
 * Singleton that controls access to the SQLiteDatabase instance
 * for this application.
 */
public class XDatabaseManager {
    private static final String TAG = "DatabaseManager";
    private static XDatabaseManager sInstance;
    private SQLiteDatabase mDatabase;


    public static synchronized XDatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new XDatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    private XDbHelper mXDbHelper;

    private XDatabaseManager(Context context) {
        mXDbHelper = new XDbHelper(context);
    }

    public Cursor queryAllDesigns(String column) {
        SQLiteDatabase db = mXDbHelper.getReadableDatabase();
        String q = "select * from " + TABLE_NAME + " ORDER BY " + column;
        Cursor result = db.rawQuery(q, null);
        return result;
    }

    public Cursor queryDesignsByName(String column, String q) {
        SQLiteDatabase db = mXDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + column + " LIKE ?";

        return db.rawQuery(query, new String[]{"%" + q + "%"});
    }

    public void addRecord(Style style) {
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(COL_1, style.STYLE_CODE);
        values.put(COL_2, style.STYLE_NAME);
        values.put(COL_3, style.SHEETS);
        values.put(COL_4, style.BACKING_CARDS);
        values.put(COL_5, style.LOCATION);

        long result = mDatabase.insert(TABLE_NAME, null, values);
        if (mDatabase.isOpen()) { mDatabase.close(); }

    }

    public void replaceRecord(Style style, String code) {
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(COL_1, style.STYLE_CODE);
        values.put(COL_2, style.STYLE_NAME);
        values.put(COL_3, style.SHEETS);
        values.put(COL_4, style.BACKING_CARDS);
        values.put(COL_5, style.LOCATION);
        values.put(COL_6, style.IS_POP);
        values.put(COL_7, style.IS_WHOLESALE);

        mXDbHelper.deleteEntry(mDatabase, code);
        long result = mDatabase.insert(TABLE_NAME, null, values);
        if (mDatabase.isOpen()) { mDatabase.close(); }
    }

    public void deleteRecord(String code) {
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        mXDbHelper.deleteEntry(mDatabase, code);
        if (mDatabase.isOpen()) { mDatabase.close(); }

    }

    public void open() throws SQLException {
        mDatabase = mXDbHelper.getWritableDatabase();
    }

}
