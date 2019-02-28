package com.example.utestotp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.utestotp.models.UserModel;
import com.example.utestotp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String USER_TABLE_NAME = "userMaster";
    public final String CONTACTS_COLUMN_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table userMaster " + "(id integer primary key, name text,phone text,time text,gps text, Asessment text,diagnosis text,medication text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertUser(String name, String phone, String time, String gps, String asessment, String diagnosis, String medication) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("time", time);
        contentValues.put("gps", gps);
        contentValues.put("Asessment", asessment);
        contentValues.put("diagnosis", diagnosis);
        contentValues.put("medication", medication);
        db.insert("userMaster", null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_TABLE_NAME);
        return numRows;
    }

    public ArrayList<UserModel> getData() throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<UserModel> array_list = new ArrayList<>();

        //hp = new HashMap();
        Cursor res = db.rawQuery("select * from userMaster", null);

        //  JSONObject jFromCursor = new JSONObject(generateObjectFromCursor(res));


        return generateObjectFromCursor(res);

    }

    public ArrayList<UserModel> generateObjectFromCursor(Cursor cursor) {
        UserModel userModel = new UserModel();
        ;
        if (cursor == null) {
            return null;
        }
        ArrayList<UserModel> userModels = new ArrayList<UserModel>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            userModel = new UserModel();
            userModel.setName(cursor.getString(cursor.getColumnIndex("name")));
            userModel.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            userModel.setTime(cursor.getString(cursor.getColumnIndex("time")));
            userModel.setGps(cursor.getString(cursor.getColumnIndex("gps")));
            userModel.setAsessment(cursor.getString(cursor.getColumnIndex("Asessment")));
            userModel.setDiagnosis(cursor.getString(cursor.getColumnIndex("diagnosis")));
            userModel.setMedication(cursor.getString(cursor.getColumnIndex("medication")));

            userModels.add(userModel);
            cursor.moveToNext();
        }


        return userModels;
    }
}
