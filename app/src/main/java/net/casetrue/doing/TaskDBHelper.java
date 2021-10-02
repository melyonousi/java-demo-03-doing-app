package net.casetrue.doing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by MOHAMED EL YONOUSI on 2/18/2021.
 */
public class TaskDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Tasks.db";
    public static final String TABLE_NAME = "Doing";
    public static final int DATABASE_VERSION = 2;
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TASK = "Task";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_TIME = "Time";

    public TaskDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table  " +
                        TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TASK + " TEXT NOT NULL,  " +
                        COLUMN_DATE + " DATE NOT NULL, " +
                        COLUMN_TIME + " TIME NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static Long createTask(SQLiteDatabase db, Task task) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task.getTask());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_TIME, task.getTime());
        long result = db.insert(TABLE_NAME, null, values);
        values.clear();
        db.close();
        return result;
    }

    public static Long updateTask(SQLiteDatabase db, Task task) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task.getTask());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_TIME, task.getTime());
        long result = db.update(TABLE_NAME, values, TaskDBHelper.COLUMN_ID + " = " + task.getId(), null);
        values.clear();
        db.close();
        return result;
    }

    public static void deleteTask(SQLiteDatabase db, long id) {
        db.delete(TaskDBHelper.TABLE_NAME, TaskDBHelper.COLUMN_ID + " = " + id, null);
        db.close();
    }

    public static ArrayList<Task> getAllTasks(SQLiteDatabase db) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY Date ASC, TIME ASC", null);
        while (cur.moveToNext()) {
            Task task = new Task();
            task.setId(Integer.parseInt(cur.getString(0)));
            task.setTask(cur.getString(1));
            task.setDate(cur.getString(2));
            task.setTime(cur.getString(3));

            tasks.add(task);
        }
        cur.close();
        db.close();
        return tasks;
    }
}
