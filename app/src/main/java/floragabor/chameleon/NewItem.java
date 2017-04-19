package floragabor.chameleon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class NewItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        Button add_btn = (Button)findViewById(R.id.add_button);

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        add_btn.startAnimation(shake);

    }
}
