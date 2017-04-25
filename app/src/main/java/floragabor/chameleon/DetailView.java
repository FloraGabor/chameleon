package floragabor.chameleon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetailView extends AppCompatActivity {

    AndroidDBHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        dbHelper = new AndroidDBHelper(this);

//        TextView tv = (TextView)findViewById(R.id.messageLabel);
//        Intent intent1 = getIntent();
//        String message = intent1.getStringExtra("message");
//        tv.setText(message);

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
        ArrayList<String> taskList = dbHelper.getTaskList();
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<String>(this, R.layout.item_list_view, R.id.item_text_view);
            lv.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) findViewById(R.id.item_text_view);
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();
    }
}
