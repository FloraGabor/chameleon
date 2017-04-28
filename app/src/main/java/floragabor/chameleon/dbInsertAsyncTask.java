package floragabor.chameleon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by O.o on 2017. 04. 28..
 */

public class dbInsertAsyncTask extends AsyncTask<String, String, String> {
    Context ctx;

    dbInsertAsyncTask (Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... params) {

        String method = params[0];
        AndroidDBHelper dbHelper = new AndroidDBHelper(ctx);
        if(method.equals("insert_item")){
            String edit_item = params[1];
            String category = params[2];
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.insertNewTask(edit_item, category);
            return "Item inserted.";
        }
        return null;
    }
}
