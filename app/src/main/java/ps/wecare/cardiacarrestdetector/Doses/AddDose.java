package ps.wecare.cardiacarrestdetector.Doses;

import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Dose;
import ps.wecare.cardiacarrestdetector.db.Medication;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class AddDose extends AppCompatActivity {

    private Boolean cancel;
    private Button submit_btn;
    private Button set_time;

    private AutoCompleteTextView time;

    private myDbAdapter helper;

    String mediccation_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dose);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText("ADd Dose");


        mediccation_id = getIntent().getStringExtra("medication_id");

        cancel = false;
        submit_btn = (Button) findViewById(R.id.submit_btn);
        time = (AutoCompleteTextView) findViewById(R.id.dose_time);

        helper = App.getInstance().getDbHelper();
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserDose();

                if (!cancel) {
                    //Intent n = new Intent(AddMedication.this, BluetoothConnectionActivity.class);
                    //AddMedication.this.startActivity(n);
                    finish();
                }
                cancel = false;
            }
        });
        set_time = (Button)findViewById(R.id.pick_time);
        set_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddDose.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute+":00");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


    }

    private void inserDose() {
        final String time22 = time.getText().toString();
        //View focusView = null;
        if (TextUtils.isEmpty(time22)) {
            time.setError(getString(R.string.error_field_required));
            //focusView = mBelovedPhoneView;
            cancel = true;
        }

        if (!cancel) {
            Dose dose = new Dose(Long.parseLong(mediccation_id), time22);
            dose = helper.insertDose(dose);
            Message.message(this, " Id : " + dose.getId());
        }
    }
}