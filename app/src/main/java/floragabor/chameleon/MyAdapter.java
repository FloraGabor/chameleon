package floragabor.chameleon;

import android.content.Context;
import android.icu.util.GregorianCalendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import floragabor.chameleon.entity.ReminderItem;

/**
 * Created by O.o on 2017. 05. 01..
 */

public class MyAdapter extends BaseAdapter {

    public void refreshList(List<ReminderItem> reminderItems) {
        this.reminderItems.clear();
        this.reminderItems.addAll(reminderItems);
    }

    public interface ItemDeletedListener {
        void onItemDeleted(long id);
    }

    private ItemDeletedListener listener;

    public void setListener(ItemDeletedListener listener) {
        this.listener = listener;
    }

    Context context;

    private final List<ReminderItem> reminderItems;

    public MyAdapter(Context context, List<ReminderItem> reminderItems) {
        this.context = context;
        this.reminderItems = reminderItems;
    }

    class ViewHolder {
        private TextView textViewText;
        private Button deleteButton;
        private TextView dueDateText;

        ViewHolder(View v) {
            textViewText = (TextView) v.findViewById(R.id.item_text_view);
            deleteButton = (Button) v.findViewById(R.id.btnDelete);
            dueDateText = (TextView)v.findViewById(R.id.duedate_text_view);
        }
    }


    @Override
    public int getCount() {
        return reminderItems.size();
    }

    @Override
    public Object getItem(int position) {
        return reminderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_list_view, parent, false);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }


        ReminderItem reminderItem = reminderItems.get(position);
        final long id = reminderItem.id;
        if(reminderItem.dueDate != 0) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(reminderItem.dueDate);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dueDateString = formatter.format(c.getTime());
            viewHolder.dueDateText.setText(dueDateString);
        }


        viewHolder.textViewText.setText(reminderItem.text);
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(id);
            }
        });

        return row;

    }

    public void deleteTask(long id) {
        if (listener != null) {
            listener.onItemDeleted(id);
        }
    }

}
