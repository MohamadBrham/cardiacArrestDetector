package ps.wecare.cardiacarrestdetector.Beloved;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class UpdateBeloved extends AppCompatActivity {

    private Button update;
    private Button delete;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView name;
    private myDbAdapter helper;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beloved_circle);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.beloved_circle_update_title);

        helper = App.getInstance().getDbHelper();
        id = getIntent().getStringExtra("Id");

        update = (Button)findViewById(R.id.submit_btn);
        update.setText("Update");
        delete = (Button)findViewById(R.id.delete_btn);
        delete.setVisibility(View.VISIBLE);
        delete.setText("Delete");

        phone = (AutoCompleteTextView) findViewById(R.id.phone_1);
        name = (AutoCompleteTextView) findViewById(R.id.name);

        phone.setText(getIntent().getStringExtra("phone"));
        name.setText(getIntent().getStringExtra("name"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.updateBeloved(Integer.parseInt(id),name.getText().toString(),phone.getText().toString());
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = helper.deleteBeloved(Integer.parseInt(id));
                if (res > 0){
                    finish();
                }

            }
        });
        //Message.message(this,"string  "+id);
    }
}
