package ps.wecare.cardiacarrestdetector;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BelovedCircleActivity extends AppCompatActivity {

    private Button submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beloved_circle);
        // make the title centered
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.app_name);

        submit_btn = (Button) findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(BelovedCircleActivity.this, BluetoothConnectionActivity.class);
                BelovedCircleActivity.this.startActivity(n);
                finish();
            }
        });


    }
}
