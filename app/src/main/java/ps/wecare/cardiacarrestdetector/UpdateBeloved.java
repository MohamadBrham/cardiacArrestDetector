package ps.wecare.cardiacarrestdetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class UpdateBeloved extends AppCompatActivity {

    Button update;
    Button delete;
    AutoCompleteTextView phone;
    private AutoCompleteTextView name;
    private myDbAdapter helper;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beloved_circle);

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.updateBeloved(Integer.parseInt(id),"",phone.getText().toString());
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
