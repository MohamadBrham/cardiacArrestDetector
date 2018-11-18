package ps.wecare.cardiacarrestdetector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Locale;

public class GuideActivity extends AppCompatActivity {
    private Button next_btn;
    private CheckBox showAgainCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!App.getInstance().showGuide()) {
            Intent n = new Intent(GuideActivity.this, LoginActivity.class);
            GuideActivity.this.startActivity(n);
            finish();
        }
        setContentView(R.layout.activity_guide);
        // make the title centered
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.app_name);

        next_btn = (Button) findViewById(R.id.next);
        showAgainCheckbox = (CheckBox) findViewById(R.id.show_again);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean dontshowAgain = showAgainCheckbox.isChecked();
                if (dontshowAgain){
                    App.getInstance().setWithoutGuide();
                }
                Intent n = new Intent(GuideActivity.this,LoginActivity.class);
                GuideActivity.this.startActivity(n);
                finish();
            }
        });
    }
}
