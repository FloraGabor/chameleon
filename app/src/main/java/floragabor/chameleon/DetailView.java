package floragabor.chameleon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailView extends AppCompatActivity {

    AndroidDBHelper dbHelper;
    ArrayList<String> taskList;
    ArrayAdapter<String> mAdapter;
    ListView lv;
    String cat;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        cat = getIntent().getStringExtra("category");
        TextView cat_tv = (TextView) findViewById(R.id.category_tv);
        cat_tv.setText(cat);
        Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Fonty.ttf");
        cat_tv.setTypeface(externalFont);
        cat_tv.setTextSize(getResources().getDimension(R.dimen.CategoryTextSize));


//        dbHelper = new AndroidDBHelper(this);

        lv = (ListView) findViewById(R.id.item_list);

//        try{
//            loadTaskList();
//        } catch (NullPointerException n){System.out.println("No item.");}


        Button btn = (Button) findViewById(R.id.plus_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DetailView.this, NewItem.class);
                intent2.putExtra("category", cat);
                DetailView.this.startActivity(intent2);
            }
        });


//        ViewHolder holder = new ViewHolder();
//        holder.listItem = (TextView)convertView.findViewById(R.id.item_text_view);
//        holder.deleteButton = (Button)convertView.findViewById(R.id.btnDelete);
//        convertView.setTag(holder);

    }

    class ViewHolder {
        private TextView listItem;
        private Button deleteButton;

        ViewHolder(View v) {
            listItem = (TextView) v.findViewById(R.id.item_text_view);
            deleteButton = (Button) v.findViewById(R.id.btnDelete);
        }
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;
        final long id = viewHolder.listItem.getId();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_list_view, parent, false);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        viewHolder.listItem.setText(dbHelper.DB_COLUMN_TEXT);
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(id);
            }
        });


        return row;

    }


    private void loadTaskList() {
        taskList = dbHelper.getTaskList(cat);
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<String>(this, R.layout.item_list_view, R.id.item_text_view);
            lv.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
        dbHelper.close();
    }


    public void deleteTask(long id) {
        TextView taskTextView = (TextView) findViewById(R.id.item_text_view);
        id = taskTextView.getId();
        dbHelper.deleteTask(id);
        loadTaskList();
        dbHelper.close();
    }
}
