package ps.wecare.cardiacarrestdetector;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.User;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class BelovedCircleActivity extends AppCompatActivity {

    private Button submit_btn;
    private AutoCompleteTextView mBelovedPhoneView;
    private boolean cancel;
    private myDbAdapter helper;
    private boolean internal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beloved_circle);
        internal = false;
        // make the title centered
        if (getIntent().getStringExtra("NEW") != null)
            internal = true;
        helper = App.getInstance().getDbHelper();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.beloved_circle_new_title);


        submit_btn = (Button) findViewById(R.id.submit_btn);
        cancel = false;
        mBelovedPhoneView = (AutoCompleteTextView) findViewById(R.id.phone_1);

        helper = App.getInstance().getDbHelper();
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertBeloved(mBelovedPhoneView);

                if (! cancel) {
                    if(!internal) {
                        Intent n = new Intent(BelovedCircleActivity.this, BluetoothConnectionActivity.class);
                        BelovedCircleActivity.this.startActivity(n);
                    }
                    finish();
                }
            }
        });


    }


    private void  insertBeloved(AutoCompleteTextView phone){
        final String phone1 = phone.getText().toString();
        View focusView = phone;
        if (TextUtils.isEmpty(phone1)){
            phone.setError(getString(R.string.error_invalid_phone));
            cancel = true;
        }
        //Message.message(this,"Before" + phone1);

        if (cancel){
            focusView.requestFocus();
        }else{
            Beloved beloved = new Beloved(App.getInstance().getUserId(),phone1,"Available");
            beloved = helper.insertBeloved(beloved);
        }
    }
}
