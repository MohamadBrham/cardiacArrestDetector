package ps.wecare.cardiacarrestdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BelovedCircleActivity extends AppCompatActivity {

    private Button submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beloved_circle);

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
