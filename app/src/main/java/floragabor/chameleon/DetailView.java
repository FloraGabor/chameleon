package floragabor.chameleon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import floragabor.chameleon.fragment.ReminderItemListFragment;

public class DetailView extends AppCompatActivity {

    private String cat;

    public void setNewItemFragment(Fragment newItemFragment) {
        this.newItemFragment = newItemFragment;
    }

    private Fragment newItemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        cat = getIntent().getStringExtra(Constans.ARG_CATEGORY);

        showListFragment();
    }

    private void showListFragment() {
        ReminderItemListFragment fragment1 = new ReminderItemListFragment();
        Bundle args = new Bundle();
        args.putString(Constans.ARG_CATEGORY, cat);
        fragment1.setArguments(args);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, fragment1).commit();
    }

    @Override
    public void onBackPressed() {

        boolean visible = false;
        if(newItemFragment != null) {
            visible = newItemFragment.isVisible();
        }

        if(visible) {
            showListFragment();
        }
        else {
            super.onBackPressed();
        }
    }
}
