package com.xxxgreen.mvx.golfdotzio;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_1;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_2;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_3;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_4;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_5;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_6;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_7;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.TABLE_NAME;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class XDbHelper extends SQLiteOpenHelper {
    private static final String TAG = XDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "golfdotz.db";
    private static final int DATABASE_VERSION = 5;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_1 + " TEXT," +
                    COL_2 + " TEXT," +
                    COL_3 + " INTEGER, " +
                    COL_4 + " INTEGER, " +
                    COL_5 + " TEXT, " +
                    COL_6 + " INTEGER, " +
                    COL_7 + " INTEGER)";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_DELETE_ENTRY =
            "DELETE FROM " + TABLE_NAME + " WHERE " + COL_1 + " = ";

    //Used to read data from res/ and assets/
    private Resources mResources;

    public XDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

        try {
            readStylesFromResources(db);
        } catch (Exception e) {
            Log.d(TAG, "Error inserting values", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    protected void deleteEntry(SQLiteDatabase db, String code) {
        db.execSQL(SQL_DELETE_ENTRY + "\'" + code + "\'");
    }

    /**
     * Streams the JSON data from sheets.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readStylesFromResources(SQLiteDatabase db) throws IOException, JSONException {
        Log.d(TAG, "reading styles from json");
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.styles);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();
        // Parse JSON data and insert into the provided database instance
        final JSONObject obj = new JSONObject(rawJson);
        final JSONArray stylesArr = obj.getJSONArray("styles");
        final int n = stylesArr.length();

        for (int i = 0; i < n; ++i) {
            final JSONObject style = stylesArr.getJSONObject(i);
            ContentValues values = new ContentValues();
            values.put(COL_1, style.getString("STYLE_CODE"));
            values.put(COL_2, style.getString("STYLE_NAME"));
            values.put(COL_3, style.getInt("SHEETS"));
            values.put(COL_4, style.getInt("BACKING_CARDS"));
            values.put(COL_5, style.getString("LOCATION"));

            long result = db.insert(TABLE_NAME, null, values);
            if (result == -1) {
                Log.d(TAG, "Error loading data");
            }
        }
    }

}