package floragabor.chameleon.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import floragabor.chameleon.AndroidDBHelper;
import floragabor.chameleon.Constans;
import floragabor.chameleon.MainActivity;
import floragabor.chameleon.MyAdapter;
import floragabor.chameleon.R;
import floragabor.chameleon.entity.ReminderItem;
import floragabor.chameleon.fragment.NewItemFragment;

/**
 * Created by O.o on 2017. 05. 03..
 */

public class ReminderItemListFragment extends Fragment implements MyAdapter.ItemDeletedListener {

    List<ReminderItem> reminderItems = new ArrayList<>();

    private TextView tvCategory;
    private Button btnAdd;
    private Button btnRefresh;
    private ListView lvReminderItems;
    private AndroidDBHelper dbHelper;
    private MyAdapter mAdapter;

    private String category = "";

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
        View root = inflater.inflate(R.layout.fragment_detail_view, null);

        tvCategory = (TextView) root.findViewById(R.id.category_tv);
        tvCategory.setText(category);
        btnAdd = (Button) root.findViewById(R.id.plus_button);
        btnRefresh = (Button) root.findViewById(R.id.btnRefresh);
        lvReminderItems = (ListView) root.findViewById(R.id.item_list);


        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Fonty.ttf");
        tvCategory.setTypeface(externalFont);
//        tvCategory.setTextSize(getResources().getDimension(R.dimen.CategoryTextSize));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewItemFragment fragment2 = new NewItemFragment();
                Bundle args = new Bundle();
                args.putString(Constans.ARG_CATEGORY, category);
                fragment2.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment2).commit();
            }
        });

        loadTaskList();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.clockwise);
                btnRefresh.startAnimation(rotate);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTaskList();
    }

    private void loadTaskList() {
        reminderItems.clear();
        reminderItems = dbHelper.getTaskList(category);
        if (mAdapter == null) {
            mAdapter = new MyAdapter(getActivity(), reminderItems);
            mAdapter.setListener(this);
            lvReminderItems.setAdapter(mAdapter);
        } else {
            mAdapter.refreshList(reminderItems);
            mAdapter.notifyDataSetChanged();
        }
        //dbHelper.close();
    }

    @Override
    public void onItemDeleted(long id) {
        dbHelper.deleteTask(id);
        loadTaskList();
    }
}
