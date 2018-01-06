package edu.fudan.ringbell.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBManager {

    private static final String TAG = DBManager.class.getName();
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private static DBManager instance = null;

}
