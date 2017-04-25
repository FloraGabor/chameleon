package floragabor.chameleon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by O.o on 2017. 04. 19..
 */

public class AndroidDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ToDoDB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "ToDoList";
    public static final String DB_COLUMN = "Items";


    public AndroidDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", TABLE_NAME, DB_COLUMN);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteTask(String task) {
        SQLiteDatabase db = this.getReadableDatabase();
        db. delete(TABLE_NAME, DB_COLUMN + " - ?", new String[]{task});
        db.close();
    }

    public ArrayList<String> getTaskList() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{DB_COLUMN}, null, null, null, null, null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }
}
