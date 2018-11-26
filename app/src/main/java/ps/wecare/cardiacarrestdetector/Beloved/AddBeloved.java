package ps.wecare.cardiacarrestdetector.Beloved;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.BluetoothConnectionActivity;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class AddBeloved extends AppCompatActivity {

    private Button submit_btn;
    private AutoCompleteTextView mBelovedPhoneView;
    private AutoCompleteTextView mBelovedNameView;
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
        mBelovedNameView = (AutoCompleteTextView) findViewById(R.id.name);

        helper = App.getInstance().getDbHelper();
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertBeloved();

                if (! cancel) {
                    if(!internal) {
                        Intent n = new Intent(AddBeloved.this, BluetoothConnectionActivity.class);
                        AddBeloved.this.startActivity(n);
                    }
                    finish();
                }
                cancel = false;
            }
        });
    }
    private void  insertBeloved(){
        final String phone = mBelovedPhoneView.getText().toString();
        final String name = mBelovedNameView.getText().toString();
        //View focusView = null;
        if (TextUtils.isEmpty(phone)){
            mBelovedPhoneView.setError(getString(R.string.error_field_required));
            //focusView = mBelovedPhoneView;
            cancel = true;
        }
        if (TextUtils.isEmpty(name)){
            mBelovedNameView.setError(getString(R.string.error_field_required));
            //focusView = mBelovedNameView;
            cancel = true;
        }
        if (!cancel){
            Beloved beloved = new Beloved(App.getInstance().getUserId(),phone,name,"Available");
            beloved = helper.insertBeloved(beloved);
            Message.message(this," Id : "+beloved.getId());
        }
    }
}
