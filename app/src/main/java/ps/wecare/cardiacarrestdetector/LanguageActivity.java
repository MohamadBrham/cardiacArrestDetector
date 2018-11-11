package ps.wecare.cardiacarrestdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LanguageActivity extends AppCompatActivity {

    private Button arabic_btn;
    private Button english_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        arabic_btn = (Button) findViewById(R.id.arabic_btn);
        english_btn = (Button) findViewById(R.id.english_btn);

        arabic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(LanguageActivity.this, GuideActivity.class);
                LanguageActivity.this.startActivity(n);
                finish();
            }
        });

        english_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(LanguageActivity.this, GuideActivity.class);
                LanguageActivity.this.startActivity(n);
                finish();
            }
        });


    }
}
