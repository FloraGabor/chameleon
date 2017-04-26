package floragabor.chameleon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

public class MainActivity extends AppCompatActivity {

    Integer[] iconIDs = {
            R.drawable.shopping_logo,
            R.drawable.books_logo,
            R.drawable.lightbulb_logo,
            R.drawable.events_logo
    };

    String[] category = {
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

//        final Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.scale);
//        gv.startAnimation(anim1);


        gv.setAdapter(new IconAdapter(this));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetailView(position);
            }
        });

        ImageView cham_iv = (ImageView) findViewById(R.id.chameleon_blind_img);
        ImageView cham_eye_iv = (ImageView) findViewById(R.id.chameleon_eye_img);

        final Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.eye_move);
        cham_eye_iv.startAnimation(anim2);

    }


    public void goToDetailView(int position){
        Intent intent = new Intent(this, DetailView.class);
        intent.putExtra("category", category[position]);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu (Menu menu){
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemsSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if(id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public class IconAdapter extends BaseAdapter{

        private Context context;

        public IconAdapter(Context ctx){
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
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView == null){
                view = new View(context);
                view = inflater.inflate(R.layout.tile, null);

                TextView tv = (TextView)view.findViewById(R.id.grid_text);
                Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Fonty.ttf");
                tv.setTypeface(externalFont);
                tv.setTextSize(getResources().getDimension(R.dimen.CategoryTextSize));


                ImageView iv = (ImageView)view.findViewById(R.id.grid_image);

                iv.setImageResource(iconIDs[position]);
                tv.setText(category[position]);
            } else {
                view = convertView;
            }

            return view;

        }
    }

}