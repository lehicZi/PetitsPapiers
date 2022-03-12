package com.example.petitspapiers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.petitspapiers.objects.Filmiz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public static final String FILMIZ_TABLE = "FILMIZ_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_TYPE = "TYPE";
    public static final String COLUMN_WATCHED = "WATCHED";
    public static final String COLUMN_TIRE_ORDER = "TIREORDER";
    public static final String COLUMN_IS_DISPO = "ISDISPO";

    public Database(@Nullable Context context) {
        super(context, "filmiz.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement = "CREATE TABLE " + FILMIZ_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TYPE + " INTEGER, " +
                COLUMN_WATCHED + "INTEGER DEFAULT 0, " +
                COLUMN_TIRE_ORDER + " INTEGER DEFAULT 0)";

        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("ALTER TABLE " + FILMIZ_TABLE + " ADD COLUMN " + COLUMN_IS_DISPO + " BOOLEAN DEFAULT 1");

    }

    public static boolean addEntry(Activity activity, Filmiz filmiz){

        final SQLiteDatabase database = new Database(activity).getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, filmiz.getTitle());
        contentValues.put(COLUMN_TYPE, filmiz.getType());
        contentValues.put(COLUMN_IS_DISPO, filmiz.isDispo());


        long insert = database.insert(FILMIZ_TABLE, null, contentValues);

        database.close();
        return insert != -1;

    }

    public static List<Filmiz> selectFilmizStatus(Activity activity, int status){

        List<Filmiz> returnList = new ArrayList<>();

        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_TYPE + ", " + COLUMN_TIRE_ORDER + ", " + COLUMN_IS_DISPO + " FROM " + FILMIZ_TABLE + " WHERE " + COLUMN_WATCHED + "=?";
        final SQLiteDatabase database = new Database(activity).getReadableDatabase();
        String[] arg =  {String.valueOf(status)};

        Cursor cursor = database.rawQuery(query,arg);

        while (cursor.moveToNext())
        {

            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            int type = cursor.getInt(2);
            int tireOrder = cursor.getInt(3);
            boolean isDispo = cursor.getInt(4) == 1;

            Filmiz filmiz = new Filmiz(title, type, isDispo);
            filmiz.setId(id);
            filmiz.setTireOrder(tireOrder);


            returnList.add(filmiz);

        }
        cursor.close();
        database.close();

        return returnList;
    }

    public static List<Filmiz> selectFilmizStatusAndType(Activity activity, int status, int type){

        List<Filmiz> returnList = new ArrayList<>();

        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_TYPE + ", " + COLUMN_TIRE_ORDER + ", " + COLUMN_IS_DISPO + " FROM " + FILMIZ_TABLE + " WHERE " + COLUMN_WATCHED + "=?" + " AND " + COLUMN_TYPE + "=?";
        final SQLiteDatabase database = new Database(activity).getReadableDatabase();
        String[] arg =  {String.valueOf(status), String.valueOf(type)};

        Cursor cursor = database.rawQuery(query,arg);

        while (cursor.moveToNext())
        {

            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            int getType = cursor.getInt(2);
            int tireOrder = cursor.getInt(3);
            boolean isDispo = cursor.getInt(4) == 1;

            Filmiz filmiz = new Filmiz(title, getType, isDispo);
            filmiz.setId(id);
            filmiz.setTireOrder(tireOrder);


            returnList.add(filmiz);

        }
        cursor.close();
        database.close();

        return returnList;
    }

    public static boolean updateStatus(Activity activity,Filmiz filmiz){

        final SQLiteDatabase database = new Database(activity).getWritableDatabase();


        final String where = COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(filmiz.getId())};
        final ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_WATCHED, filmiz.getStatus());

        final boolean updated = database.update(FILMIZ_TABLE, contentValues, where, whereArgs) != -1;

        database.close();


        return updated;

    }

    public static boolean updateTireOrder(Activity activity,Filmiz filmiz){

        final SQLiteDatabase database = new Database(activity).getWritableDatabase();


        final String where = COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(filmiz.getId())};
        final ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TIRE_ORDER, filmiz.getTireOrder());

        final boolean updated = database.update(FILMIZ_TABLE, contentValues, where, whereArgs) != -1;

        database.close();


        return updated;

    }

    public static boolean deleteEntry(Activity activity, Filmiz filmiz){

        final SQLiteDatabase database = new Database(activity).getWritableDatabase();


        final String where = COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(filmiz.getId())};


        final boolean deleted = database.delete(FILMIZ_TABLE, where, whereArgs) == 1;

        database.close();


        return deleted;


    }

    public static boolean renameEntry(Activity activity, Filmiz filmiz, String newName){

        final SQLiteDatabase database = new Database(activity).getWritableDatabase();


        final String where = COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(filmiz.getId())};

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, newName);


        final boolean updated = database.update(FILMIZ_TABLE, cv, where, whereArgs) == 1;

        database.close();


        return updated;


    }

    public static boolean updateDispo(Activity activity,Filmiz filmiz){

        final SQLiteDatabase database = new Database(activity).getWritableDatabase();


        final String where = COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(filmiz.getId())};
        final ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_IS_DISPO, filmiz.isDispo());

        final boolean updated = database.update(FILMIZ_TABLE, contentValues, where, whereArgs) != -1;

        database.close();


        return updated;

    }



    public boolean exportDatabase(String exportFileName) throws IOException {
        // Get directories
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        // Create files handles
        File newDb = new File(getWritableDatabase().getPath());
        File exportedFile = new File(sd, exportFileName);
        if (newDb.exists()) {
            System.err.println("Export DB " + exportedFile.getCanonicalPath() + " -->" + exportFileName);
            Utils.copyFile(
                    new FileInputStream(newDb),
                    new FileOutputStream(exportedFile));
            return true;
        }
        return false;
    }

    public boolean importDatabase(String exportFileName) throws IOException {
        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();
        File sd = Environment.getExternalStorageDirectory();
        File newDb = new File(sd, exportFileName);
        File oldDb = new File(getWritableDatabase().getPath());
        if (newDb.exists()) {
            System.err.println("Import DB " + oldDb.getCanonicalPath() + "<--" + exportFileName );
            Utils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
        // Access the copied database so SQLiteHelper will cache it and mark
        // it as created.
            getWritableDatabase();
            return true;
        }
        return false;
    }





}
