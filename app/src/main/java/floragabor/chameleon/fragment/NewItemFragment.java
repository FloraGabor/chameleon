package floragabor.chameleon.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import floragabor.chameleon.AndroidDBHelper;
import floragabor.chameleon.Constans;
import floragabor.chameleon.DetailView;
import floragabor.chameleon.R;
import floragabor.chameleon.entity.ReminderItem;
import floragabor.chameleon.notification.NotiService;
import floragabor.chameleon.notification.NotificationBroadcast;

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
    private int sYear, sMonth, sDay, sHour, sMinute;

    long dateSaved;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        dbHelper = new AndroidDBHelper(context);

        ((DetailView) getActivity()).setNewItemFragment(this);
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

        TextView categoryTextView = (TextView) root.findViewById(R.id.category_tv);
        categoryTextView.setText(category);
        Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Fonty.ttf");
        categoryTextView.setTypeface(externalFont);


        TextView newItemTitle = (TextView) root.findViewById(R.id.new_item_title);
        Typeface externalFont2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Fonty.ttf");
        newItemTitle.setTypeface(externalFont);
        newItemTitle.setTextSize(getResources().getDimension(R.dimen.NewItemTextSize));

        editTextNewItem = (EditText) root.findViewById(R.id.new_item_edit);
        btnAdd = (Button) root.findViewById(R.id.add_button);

        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        btnAdd.startAnimation(shake);

        btnDatePicker = (Button) root.findViewById(R.id.btnDateSelect);
        btnTimePicker = (Button) root.findViewById(R.id.btnTimeSelect);
        txtDate = (EditText) root.findViewById(R.id.in_date);
        txtTime = (EditText) root.findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        ((DetailView) getActivity()).setNewItemFragment(null);
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            final int year = c.get(Calendar.YEAR);
            final int month = c.get(Calendar.MONTH);
            final int day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            sYear = year;
                            sMonth = monthOfYear;
                            sDay = dayOfMonth;

                            String dateText = String.format("%d - %02d - %02d", sYear, sMonth + 1, sDay);
                            txtDate.setText(dateText);
                        }
                    }, year, month, day);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            final int minute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            sHour = hourOfDay;
                            sMinute = minute;

                            String timeText = String.format("%02d : %02d", sHour, sMinute);
                            txtTime.setText(timeText);
                        }
                    }, hour, minute, true);

            timePickerDialog.show();
        }
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
                    dateSaved = dueDateToTimestamp(sYear, sMonth, sDay, sHour, sMinute);
                    new InsertTask(getActivity()).execute();
                }
            }
        });
    }

    private long insertNewItem() {
        String text = editTextNewItem.getText().toString();

        ReminderItem reminderItem = new ReminderItem();
        reminderItem.text = text;
        reminderItem.category = category;
        reminderItem.dueDate = dateSaved;
        reminderItem.id = dbHelper.insertNewTask(reminderItem);
        return reminderItem.id;
    }

    private long dueDateToTimestamp(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return (c.getTimeInMillis());
    }

    private class InsertTask extends AsyncTask<Void, Void, Long> {

        private Activity activity;

        InsertTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Long doInBackground(Void... params) {
            return insertNewItem();
        }

        @Override
        protected void onPostExecute(Long id) {

            Toast.makeText(activity, "Reminder item inserted.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(activity, NotificationBroadcast.class);
            intent.putExtra(Constans.EXTRA_REMINDER_ITEM_ID, id);
            intent.putExtra(Constans.EXTRA_CATEGORY_NAME, category);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, Constans.REQUEST_CODE_NOTI, intent, 0);

            AlarmManager am = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
            long currentTimeMillis = System.currentTimeMillis();
            long halfAnHour = 30 * 60 * 1000;
            long notiTime;
            long delay;
            if (dateSaved - currentTimeMillis > halfAnHour) {
                notiTime = dateSaved - halfAnHour;
                delay = dateSaved - currentTimeMillis - halfAnHour;
            } else {
                notiTime = dateSaved;
                delay = dateSaved - currentTimeMillis;
            }

            Intent serviceIntent = new Intent(activity, NotiService.class);
            serviceIntent.putExtra(Constans.EXTRA_DELAY, delay);
            serviceIntent.putExtra(Constans.EXTRA_REMINDER_ITEM_ID, id);
            serviceIntent.putExtra(Constans.EXTRA_CATEGORY_NAME, category);
            activity.startService(serviceIntent);

            //am.set(AlarmManager.RTC_WAKEUP, notiTime, pendingIntent);

            activity.onBackPressed();
        }
    }
}
