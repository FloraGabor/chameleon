package floragabor.chameleon.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import floragabor.chameleon.AndroidDBHelper;
import floragabor.chameleon.Constans;
import floragabor.chameleon.MainActivity;
import floragabor.chameleon.R;
import floragabor.chameleon.entity.ReminderItem;

/**
 * Created by O.o on 2017. 05. 07..
 */

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long id = intent.getLongExtra(Constans.EXTRA_REMINDER_ITEM_ID, -1);
        String category = intent.getStringExtra(Constans.EXTRA_CATEGORY_NAME);

        if (id == -1 || category == null) {
            return;
        }

        AndroidDBHelper dbHelper = new AndroidDBHelper(context);

        List<ReminderItem> reminderItemList = dbHelper.getTaskList(category);
        ReminderItem reminderItem = null;
        for(ReminderItem item : reminderItemList) {
            if(item.id == id) {
                reminderItem = item;
                break;
            }
        }

        if(reminderItem == null) {
            return;
        }

        long quarterHour = 15 * 60 * 1000;
        long currentTime = System.currentTimeMillis();
        long dueDate = reminderItem.dueDate;

        String contentText;
        if(dueDate - currentTime > quarterHour) {
            contentText = "In half an hour";
        } else {
            contentText = "Now";
        }

        Intent notiIntent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, notiIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.chameleon_logo)
                .setContentTitle("Chameleon To-Do: " + reminderItem.text)
                .setContentText(contentText)
//                .setVibrate(pattern)
                .setAutoCancel(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }
}
