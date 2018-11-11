package ps.wecare.cardiacarrestdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class GuideActivity extends AppCompatActivity {
    private Button next_btn;
    private CheckBox showAgainCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        next_btn = (Button) findViewById(R.id.next);
        showAgainCheckbox = (CheckBox) findViewById(R.id.show_again);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean showAgain = showAgainCheckbox.isChecked();

                Intent n = new Intent(GuideActivity.this,LoginActivity.class);
                GuideActivity.this.startActivity(n);
                finish();
            }
        });
    }
}
