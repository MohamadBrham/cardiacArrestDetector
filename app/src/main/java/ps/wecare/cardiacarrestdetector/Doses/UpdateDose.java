package ps.wecare.cardiacarrestdetector.Doses;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.Config;
import ps.wecare.cardiacarrestdetector.Medications.UpdateMedication;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Dose;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

import static ps.wecare.cardiacarrestdetector.Config.BASE_URL;

public class UpdateDose extends AppCompatActivity {
    private Button update;
    private Button delete;
    private Button set_time;
    private AutoCompleteTextView time;
    private myDbAdapter helper;
    private String id;

    private StringRequest request;
    private String url = BASE_URL+"doses/";
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
                updateDose(new Dose(Integer.parseInt(id),1,time.getText().toString()));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int res = helper.deleteDose(Integer.parseInt(id));

                deleteDose();

                finish();

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

    private void deleteDose() {
        request = new StringRequest(Request.Method.DELETE, url+ id+"/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);

                    if(jsonObject.names().get(0).equals("deleted")){
                        Message.message(UpdateDose.this, "Order Submitted");
                    }
                    else{
                        Message.message(UpdateDose.this, "Creation Failed Wrong Input");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message.message(UpdateDose.this, "ERROR");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        App.getInstance().getRequestQueue().add(request);

    }


    private void updateDose(final Dose dose) {
        request =new StringRequest(Request.Method.PATCH, url+ id+"/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);

                    if(jsonObject.names().get(0).equals("updated")){
                        Message.message(UpdateDose.this, "Order Submitted");
                    }
                    else{
                        Message.message(UpdateDose.this, "Creation Failed Wrong Input");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message.message(UpdateDose.this, "ERROR");
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
                hashMap.put("time",dose.getTime()+"");
                return hashMap;
            }
        };
        App.getInstance().getRequestQueue().add(request);

    }


}
