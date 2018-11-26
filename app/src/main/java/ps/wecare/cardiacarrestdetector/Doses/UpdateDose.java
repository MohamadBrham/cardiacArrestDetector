package ps.wecare.cardiacarrestdetector.Doses;

import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.Medications.UpdateMedication;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Dose;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class UpdateDose extends AppCompatActivity {
    private Button update;
    private Button delete;
    private Button set_time;
    private AutoCompleteTextView time;
    private myDbAdapter helper;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dose);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText("Update Dose");

        helper = App.getInstance().getDbHelper();
        id = getIntent().getStringExtra("Id");

        update = (Button)findViewById(R.id.submit_btn);
        update.setText("Update");
        delete = (Button)findViewById(R.id.delete_btn);
        delete.setVisibility(View.VISIBLE);
        delete.setText("Delete");

        time = (AutoCompleteTextView) findViewById(R.id.dose_time);

        time.setText(getIntent().getStringExtra("time"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.updateDose(new Dose(Integer.parseInt(id),1,time.getText().toString()));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = helper.deleteDose(Integer.parseInt(id));
                if (res > 0){
                    finish();
                }

            }
        });
        //Message.message(this,"string  "+id);

        set_time = (Button)findViewById(R.id.pick_time);
        set_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateDose.this, new TimePickerDialog.OnTimeSetListener() {
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
}
