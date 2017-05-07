package floragabor.chameleon.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import floragabor.chameleon.AndroidDBHelper;
import floragabor.chameleon.Constans;
import floragabor.chameleon.R;
import floragabor.chameleon.entity.ReminderItem;
import floragabor.chameleon.notification.NotiService;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by O.o on 2017. 05. 03..
 */

public class NewItemFragment extends Fragment implements View.OnClickListener {

    private String category;

    private EditText editTextNewItem;
    private Button btnAdd;
    private AndroidDBHelper dbHelper;

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int sYear, sMonth, sDay, sHour, sMinute;

    long dateSaved;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dbHelper = new AndroidDBHelper(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        category = getArguments().getString(Constans.ARG_CATEGORY);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_item, null);

        TextView categoryTextView = (TextView)root.findViewById(R.id.category_tv);
        categoryTextView.setText(category);
        Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Fonty.ttf");
        categoryTextView.setTypeface(externalFont);
        categoryTextView.setTextSize(getResources().getDimension(R.dimen.CategoryTextSize));


        TextView newItemTitle = (TextView)root.findViewById(R.id.new_item_title);
        Typeface externalFont2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Fonty.ttf");
        newItemTitle.setTypeface(externalFont);
        newItemTitle.setTextSize(getResources().getDimension(R.dimen.NewItemTextSize));

        editTextNewItem = (EditText) root.findViewById(R.id.new_item_edit);
        btnAdd = (Button) root.findViewById(R.id.add_button);

        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        btnAdd.startAnimation(shake);

        btnDatePicker=(Button)root.findViewById(R.id.btnDateSelect);
        btnTimePicker=(Button)root.findViewById(R.id.btnTimeSelect);
        txtDate=(EditText)root.findViewById(R.id.in_date);
        txtTime=(EditText)root.findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);


        return root;
    }


    @Override
    public void onClick(View v){

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            sYear = year;
                            sMonth = monthOfYear;
                            sDay = dayOfMonth;

                            txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            sHour = hourOfDay;
                            sMinute = minute;

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);

            timePickerDialog.show();
        }

        dateSaved = dueDateToTimestamp(sYear, sMonth, sDay, sHour, sMinute);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_item = String.valueOf(editTextNewItem.getText());
                if (TextUtils.isEmpty(edit_item)) {
                    editTextNewItem.setError("Please enter something.");
                    return;
                } else {
                    insertNewItem();
                    Toast.makeText(getActivity(), "Reminder item inserted.", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().
                        remove(NewItemFragment.this).commit();

//                    getActivity().onBackPressed();
                }
            }
        });
    }

    private void insertNewItem() {
        String text = editTextNewItem.getText().toString();

        ReminderItem reminderItem = new ReminderItem();
        reminderItem.text = text;
        reminderItem.category = category;
        reminderItem.dueDate = dateSaved;
        reminderItem.id = dbHelper.insertNewTask(reminderItem);
    }

    private long dueDateToTimestamp(int year, int month, int day, int hour, int minute){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return  (c.getTimeInMillis());
    }


}
