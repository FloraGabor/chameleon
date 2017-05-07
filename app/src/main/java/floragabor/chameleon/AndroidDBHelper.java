package floragabor.chameleon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import floragabor.chameleon.entity.ReminderItem;

/**
 * Created by O.o on 2017. 04. 19..
 */

public class AndroidDBHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "ToDoDB";
    public static final int DB_VERSION = 3;
    public static final String TABLE_NAME = "ToDoList";
    public static final String DB_COLUMN_ID = "ID";
    public static final String DB_COLUMN_CATEGORY = "Category";
    public static final String DB_COLUMN_TEXT = "Text";
    public static final String DB_COLUMN_DUE_DATE = "DueDate";

    public AndroidDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER);", TABLE_NAME, DB_COLUMN_ID, DB_COLUMN_CATEGORY, DB_COLUMN_TEXT, DB_COLUMN_DUE_DATE);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public long insertNewTask(ReminderItem reminderItem) {
        return insertNewTask(reminderItem.text, reminderItem.category, reminderItem.dueDate);
    }

    public long insertNewTask(String text, String category, Long dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_TEXT, text);
        values.put(DB_COLUMN_CATEGORY, category);
        if (dueDate != null) {
            values.put(DB_COLUMN_DUE_DATE, dueDate);
        }
        long id = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return id;
    }

    public void deleteTask(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, DB_COLUMN_ID + " = ?", new String[]{Long.toString(id)});
        db.close();
    }

    public ArrayList<ReminderItem> getTaskList(String category) {
        ArrayList<ReminderItem> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, DB_COLUMN_CATEGORY + " = ?", new String[]{category}, null, null, null);
        while (cursor.moveToNext()) {

            ReminderItem reminderItem = new ReminderItem();
            reminderItem.id = cursor.getLong(cursor.getColumnIndex(DB_COLUMN_ID));
            reminderItem.category = cursor.getString(cursor.getColumnIndex(DB_COLUMN_CATEGORY));
            reminderItem.text = cursor.getString(cursor.getColumnIndex(DB_COLUMN_TEXT));
            reminderItem.dueDate = cursor.getLong(cursor.getColumnIndex(DB_COLUMN_DUE_DATE));
            taskList.add(reminderItem);
        }
        cursor.close();
        db.close();
        return taskList;
    }
}
