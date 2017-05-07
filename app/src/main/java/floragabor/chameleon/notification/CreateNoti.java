package floragabor.chameleon.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import floragabor.chameleon.entity.ReminderItem;
import floragabor.chameleon.fragment.NewItemFragment;

/**
 * Created by O.o on 2017. 05. 06..
 */

public class CreateNoti extends AppCompatActivity {

    ReminderItem reminderItem;
//    long notiTime = reminderItem.dueDate - 70740;
    long currentTime = (long)(System.currentTimeMillis() + 5000);

    long notiTime = currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 001, intent, 0);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, notiTime, pendingIntent);

//        if (notiTime == currentTime){
//            startService(new Intent(this, NotiService.class));
//        }

    }
}
