package floragabor.chameleon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public abstract class DetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        TextView tv = (TextView)findViewById(R.id.messageLabel);
        Intent intent1 = getIntent();
        String message = intent1.getStringExtra("message");
        tv.setText(message);

        ListView lv = (ListView)findViewById(R.id.item_list);

        Button btn = (Button)findViewById(R.id.plus_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DetailView.this, NewItem.class);
                DetailView.this.startActivity(intent2);
            }
        });

    }


}
