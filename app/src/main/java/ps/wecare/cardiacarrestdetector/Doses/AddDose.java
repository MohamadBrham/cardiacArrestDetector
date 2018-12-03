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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Dose;
import ps.wecare.cardiacarrestdetector.db.Medication;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

import static ps.wecare.cardiacarrestdetector.Config.BASE_URL;

public class AddDose extends AppCompatActivity {

    private Boolean cancel;
    private Button submit_btn;
    private Button set_time;

    private AutoCompleteTextView time;

    private myDbAdapter helper;

    String mediccation_id;

    private StringRequest request;
    private String url = BASE_URL+"doses/";


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
            //dose = helper.insertDose(dose);
            sendOrder(dose);
            //Message.message(this, " Id : " + dose.getId());
        }
    }
    private void sendOrder(final Dose dose) {
         request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);

                    if(jsonObject.names().get(0).equals("added")){
                        Message.message(AddDose.this, "Order Submitted");
                    }
                    else{
                        Message.message(AddDose.this, "Creation Failed Wrong Input");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message.message(AddDose.this, "ERROR");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<String, String>();
                hashMap.put("medication_id",dose.getMedication_id()+"");
                hashMap.put("time",dose.getTime()+"");
                return hashMap;
            }
        };
        App.getInstance().getRequestQueue().add(request);

    }

}