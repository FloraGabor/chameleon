package floragabor.chameleon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by O.o on 2017. 04. 19..
 */

public class AndroidDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ToDoDB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "ToDoList";


    public static class ToDosEntry implements BaseColumns {
        public static final String TABLE_NAME = "ToDoList";
        public static final String COLUMN_NAME_SHOP = "shopping";
        public static final String COLUMN_NAME_WORK = "work";
        public static final String COLUMN_NAME_IDEA = "ideas";
        public static final String COLUMN_NAME_EVENT = "events";
    }

    private static final String SQL_SHOP_ENTRIES =
            "CREATE TABLE " + ToDosEntry.TABLE_NAME + " (" +
                    ToDosEntry.COLUMN_NAME_SHOP + " TEXT PRIMARY KEY," + ")";

    private static final String SQL_WORK_ENTRIES =
            "CREATE TABLE " + ToDosEntry.TABLE_NAME + " (" +
                    ToDosEntry.COLUMN_NAME_WORK + " TEXT PRIMARY KEY," + ")";

    private static final String SQL_IDEA_ENTRIES =
            "CREATE TABLE " + ToDosEntry.TABLE_NAME + " (" +
                    ToDosEntry.COLUMN_NAME_IDEA + " TEXT PRIMARY KEY," + ")";

    private static final String SQL_EVENT_ENTRIES =
            "CREATE TABLE " + ToDosEntry.TABLE_NAME + " (" +
                    ToDosEntry.COLUMN_NAME_EVENT + " TEXT PRIMARY KEY," + ")";


    public AndroidDBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CreateTable = "CREATE TABLE" + TABLE_NAME;
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME );
        onCreate(db);
    }
}
