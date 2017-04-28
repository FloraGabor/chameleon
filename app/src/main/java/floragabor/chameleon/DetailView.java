package floragabor.chameleon;

import android.content.Context;
import android.content.Intent;
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
    ArrayAdapter<String> mAdapter;
    ListView lv;
    String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        cat = getIntent().getStringExtra("category");
        TextView cat_tv = (TextView)findViewById(R.id.category_tv);
        cat_tv.setText(cat);


//        dbHelper = new AndroidDBHelper(this);

        lv = (ListView) findViewById(R.id.item_list);

        try{
            loadTaskList();
        } catch (NullPointerException n){System.out.println("No item.");}


        Button btn = (Button) findViewById(R.id.plus_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DetailView.this, NewItem.class);
                intent2.putExtra("category", cat);
                DetailView.this.startActivity(intent2);
            }
        });





    }

    public class listAdapter extends BaseAdapter{

        private Context context;

        public listAdapter(Context ctx){
            context = ctx;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_list_view, null);
                viewHolder.listItem = (TextView)convertView.findViewById(R.id.item_text_view);
                viewHolder.deleteButton = (Button)convertView.findViewById(R.id.btnDelete);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            // ide hiányzik még valami


            return convertView;

        }
    }

    static class ViewHolder{
        private TextView listItem;
        private Button deleteButton;
    }

    private void loadTaskList() {
        ArrayList<String> taskList = dbHelper.getTaskList(cat);
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
