package floragabor.chameleon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailView extends AppCompatActivity {

    ArrayList<String> taskList;
    AndroidDBHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lv;
    String cat;
    Context context = this;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_view);

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

    }


    private void loadTaskList() {
        //taskList = dbHelper.getTaskList(cat);
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



}
