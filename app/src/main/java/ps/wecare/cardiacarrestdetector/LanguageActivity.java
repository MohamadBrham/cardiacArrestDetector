package ps.wecare.cardiacarrestdetector;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    private Button arabic_btn;
    private Button english_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        // make the title centered
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.app_name);

        arabic_btn = (Button) findViewById(R.id.arabic_btn);
        english_btn = (Button) findViewById(R.id.english_btn);

        arabic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = new Locale("ar");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.setLocale(locale);
                Resources resources = getResources();
                resources.updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

                Intent n = new Intent(LanguageActivity.this, GuideActivity.class);
                LanguageActivity.this.startActivity(n);
                //finish();
            }
        });

        english_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.setLocale(locale);
                Resources resources = getResources();
                resources.updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                Intent n = new Intent(LanguageActivity.this, GuideActivity.class);
                LanguageActivity.this.startActivity(n);
               //finish();
            }
        });


    }
}
