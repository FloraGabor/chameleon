package floragabor.chameleon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewItem extends AppCompatActivity {

    AndroidDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        Button add_btn = (Button) findViewById(R.id.add_button);

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        add_btn.startAnimation(shake);

        TextView tv = (TextView) findViewById(R.id.new_item_title);

        final EditText et = (EditText) findViewById(R.id.new_item_edit);

//        add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String edit_item = String.valueOf(et.getText());
//                dbHelper.insertNewTask(edit_item);
//            }
//        });

    }
}
