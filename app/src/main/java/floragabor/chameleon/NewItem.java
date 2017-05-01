package floragabor.chameleon;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewItem extends AppCompatActivity {

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        category = getIntent().getStringExtra("category");
        final TextView cat_tv = (TextView) findViewById(R.id.category_tv);
        cat_tv.setText(category);
        Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Fonty.ttf");
        cat_tv.setTypeface(externalFont);
        cat_tv.setTextSize(getResources().getDimension(R.dimen.CategoryTextSize));


        Button add_btn = (Button) findViewById(R.id.add_button);

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        add_btn.startAnimation(shake);


        TextView tv = (TextView) findViewById(R.id.new_item_title);
        externalFont = Typeface.createFromAsset(getAssets(), "fonts/Fonty.ttf");
        tv.setTypeface(externalFont);
        tv.setTextSize(getResources().getDimension(R.dimen.NewItemTextSize));

        final EditText et = (EditText) findViewById(R.id.new_item_edit);


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_item = String.valueOf(et.getText());
                if (TextUtils.isEmpty(edit_item)) {
                    et.setError("Please enter something.");
                    return;
                } else {
                    dbInsertAsyncTask insertAsyncTask = new dbInsertAsyncTask(NewItem.this);
                    insertAsyncTask.execute("insert_item", edit_item, category);
                    finish();
                }

            }
        });

    }
}


