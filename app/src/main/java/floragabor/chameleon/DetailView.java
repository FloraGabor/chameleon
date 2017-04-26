package floragabor.chameleon;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetailView extends AppCompatActivity {

    AndroidDBHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lv;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        dbHelper = new AndroidDBHelper(this);

        TextView tv = (TextView)findViewById(R.id.category_tv);
        Intent intent1 = getIntent();
        category = intent1.getStringExtra("category");
        tv.setText(category);

        lv = (ListView) findViewById(R.id.item_list);

        loadTaskList();

        Button btn = (Button) findViewById(R.id.plus_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DetailView.this, NewItem.class);
                DetailView.this.startActivity(intent2);
            }
        });

    }

    private void loadTaskList() {
        ArrayList<String> taskList = dbHelper.getTaskList(category);
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<String>(this, R.layout.item_list_view, R.id.item_text_view);
            lv.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }


    public void deleteTask(long id) {
        TextView taskTextView = (TextView) findViewById(R.id.item_text_view);
        id = taskTextView.getId();
        dbHelper.deleteTask(id);
        loadTaskList();
    }
}
