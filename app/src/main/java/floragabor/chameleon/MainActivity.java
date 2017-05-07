package floragabor.chameleon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import floragabor.chameleon.fragment.ReminderItemListFragment;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragments = new ArrayList<>();

    Integer[] iconIDs = {
            R.drawable.shopping_logo,
            R.drawable.books_logo,
            R.drawable.lightbulb_logo,
            R.drawable.events_logo
    };

    static String[] category = {
            "Shopping",
            "Work",
            "Ideas",
            "Events"
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gv = (GridView) findViewById(R.id.grid_view);


        gv.setAdapter(new IconAdapter(this));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //goToDetailView(position); //commented
                showList((String) view.getTag());
            }
        });

        ImageView cham_iv = (ImageView) findViewById(R.id.chameleon_blind_img);
        ImageView cham_eye_iv = (ImageView) findViewById(R.id.chameleon_eye_img);

        final Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.eye_move);
        cham_eye_iv.startAnimation(anim2);

    }

    public void goToDetailView(int position) {
        Intent intent = new Intent(this, DetailView.class);
        intent.putExtra("category", category[position]);
        startActivity(intent);

        getSupportFragmentManager().popBackStack();
    }

    private void showList(String category) {
        ReminderItemListFragment fragment = new ReminderItemListFragment();
        Bundle args = new Bundle();
        args.putString(Constans.ARG_CATEGORY, category);
        fragment.setArguments(args);

        addFragment(fragment);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit(); //commented
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);

        int size = fragments.size();
        showFragment(fragments.get(size - 1));
    }

    private void removeFragment(Fragment fragment) {
        if (fragments.size() > 1) {
            int size = fragments.size();
            replaceFragment(fragments.get(size - 2));
        } else if (fragments.size() > 0) {
            int size = fragments.size();
            removeFragment(fragments.get(size - 1));
        } else {

            fragments.remove(fragment);
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

//    @Override
//    public void onBackPressed() {
//
//        //int count = getSupportFragmentManager().getBackStackEntryCount(); //commented!!
//
//        if (fragments.size() > 0) {
//            removeFragment(fragments.get(fragments.size() - 1));
//            //getSupportFragmentManager().popBackStack(); //commented!!
//        } else {
//            super.onBackPressed();
//        }
//    }


    public class IconAdapter extends BaseAdapter {

        private Context context;

        public IconAdapter(Context ctx) {
            context = ctx;
        }

        @Override
        public int getCount() {
            return iconIDs.length;
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
            View view;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                view = new View(context);
                view = inflater.inflate(R.layout.tile, null);

                TextView tv = (TextView) view.findViewById(R.id.grid_text);
                Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Fonty.ttf");
                tv.setTypeface(externalFont);
                tv.setTextSize(getResources().getDimension(R.dimen.CategoryTextSize));


                ImageView iv = (ImageView) view.findViewById(R.id.grid_image);

                iv.setImageResource(iconIDs[position]);
                tv.setText(category[position]);
                view.setTag(category[position]);
            } else {
                view = convertView;
            }

            return view;

        }
    }

}