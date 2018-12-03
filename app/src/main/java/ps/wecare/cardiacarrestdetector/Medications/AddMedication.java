package ps.wecare.cardiacarrestdetector.Medications;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.Beloved.AddBeloved;
import ps.wecare.cardiacarrestdetector.BluetoothConnectionActivity;
import ps.wecare.cardiacarrestdetector.Doses.AddDose;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Dose;
import ps.wecare.cardiacarrestdetector.db.Medication;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

import static ps.wecare.cardiacarrestdetector.Config.BASE_URL;

public class AddMedication extends AppCompatActivity {

    private int mYear,mMonth,mDay;
    private AutoCompleteTextView first_date;
    private AutoCompleteTextView last_date;
    private AutoCompleteTextView name;
    private Button set_first_Date;
    private Button set_last_Date;
    private Calendar myCalendar;
    private  Calendar c;

    private Button submit_btn;
    private myDbAdapter helper;
    private boolean cancel;


    private StringRequest request;
    private String url = BASE_URL+"medications/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.add_medication_btn);

        cancel = false;
        submit_btn = (Button) findViewById(R.id.submit_btn);
        name = (AutoCompleteTextView) findViewById(R.id.name);
        first_date = (AutoCompleteTextView) findViewById(R.id.medication_start_date);
        last_date = (AutoCompleteTextView) findViewById(R.id.medication_end_date);
        set_first_Date = (Button) findViewById(R.id.pick_start_date);
        set_last_Date = (Button) findViewById(R.id.pick_last_date);

        helper = App.getInstance().getDbHelper();
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertMedication();

                if (! cancel) {
                        //Intent n = new Intent(AddMedication.this, BluetoothConnectionActivity.class);
                        //AddMedication.this.startActivity(n);
                    finish();
                }
                cancel = false;
            }
        });

        c  = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


       set_first_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(AddMedication.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                if (year < mYear)
                                    view.updateDate(mYear,mMonth,mDay);
                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear,mMonth,mDay);
                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear,mMonth,mDay);
                                first_date.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });
        set_last_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(AddMedication.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                if (year < mYear)
                                    view.updateDate(mYear,mMonth,mDay);
                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear,mMonth,mDay);
                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear,mMonth,mDay);
                                last_date.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });

    }
    private void  insertMedication(){
        final String name2 = name.getText().toString();
        final String start = first_date.getText().toString();
        final String end = last_date.getText().toString();
        //View focusView = null;
        if (TextUtils.isEmpty(name2)){
            name.setError(getString(R.string.error_field_required));
            //focusView = mBelovedPhoneView;
            cancel = true;
        }
        if (TextUtils.isEmpty(start)){
            first_date.setError(getString(R.string.error_field_required));
            //focusView = mBelovedNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(end)){
            last_date.setError(getString(R.string.error_field_required));
            //focusView = mBelovedNameView;
            cancel = true;
        }
        if (!cancel){
            Medication medication = new Medication(App.getInstance().getUserId(),name2,start,end);
            //edication = helper.insertMedicatin(medication);
            //Message.message(this," Id : "+medication.getId());
            sendOrder(medication);
        }
    }

    private void sendOrder(final Medication medication) {
        request =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    Message.message(AddMedication.this, "Order SENT ");
                    if(jsonObject.names().get(0).equals("added")){
                        Message.message(AddMedication.this, "Order Submitted");
                    }
                    else{
                        Message.message(AddMedication.this, "Creation Failed Wrong Input");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Message.message(AddMedication.this, "ERROR");
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
                hashMap.put("name",medication.getName()+"");
                hashMap.put("user_id",medication.getUser_id()+"");
                hashMap.put("start",medication.getStart()+"");
                hashMap.put("end",medication.getEnd()+"");
                return hashMap;
            }
        };
        App.getInstance().getRequestQueue().add(request);

    }


}
