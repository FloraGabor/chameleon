package floragabor.chameleon;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class NewItem extends AppCompatActivity {

    String category;
//    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_item);

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

        final Button btnDatePicker = (Button)findViewById(R.id.btnDateSelect);
//        Button btnTimePicker = (Button)findViewById(R.id.btnTimeSelect);
        final EditText textDate = (EditText)findViewById(R.id.in_date);
//        EditText textTime = (EditText)findViewById(R.id.in_time);

        // picker onClickListeners

//        btnDatePicker.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if (v == btnDatePicker){
//                    final Calendar c = Calendar.getInstance();
//                    mYear = c.get(Calendar.YEAR);
//                    mMonth = c.get(Calendar.MONTH);
//                    mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                            new DatePickerDialog.OnDateSetListener() {
//
//                                @Override
//                                public void onDateSet(DatePicker view, int year,
//                                                      int monthOfYear, int dayOfMonth) {
//
//                                    textDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//
//                                }
//                            }, mYear, mMonth, mDay);
//                    datePickerDialog.show();
//                }
//
//                }
//            }
//        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_item = String.valueOf(et.getText());
                if (TextUtils.isEmpty(edit_item)) {
                    et.setError("Please enter something.");
                    return;
                } else {
                    DBInsertAsyncTask insertAsyncTask = new DBInsertAsyncTask(NewItem.this);
                    insertAsyncTask.execute("insert_item", edit_item, category);
                    finish();
                }

            }
        });

    }
}


