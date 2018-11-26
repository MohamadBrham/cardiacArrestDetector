package ps.wecare.cardiacarrestdetector.Medications;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.Doses.AddDose;
import ps.wecare.cardiacarrestdetector.Doses.UpdateDose;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Dose;
import ps.wecare.cardiacarrestdetector.db.Medication;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class UpdateMedication extends AppCompatActivity {
    private int mYear,mMonth,mDay;
    private  Calendar c;


    private Button update;
    private Button delete;
    private Button add_dose;
    private Button set_first_Date;
    private Button set_last_Date;
    private AutoCompleteTextView name;
    private AutoCompleteTextView start_date;
    private AutoCompleteTextView end_date;
    private myDbAdapter helper;
    private String medication_id;


    // Doses List View
    private ListView dosesListView;
    private ArrayList<Dose> doses;
    private ArrayList<String> values;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_medication);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.update_medication_title);

        helper = App.getInstance().getDbHelper();
        medication_id = getIntent().getStringExtra("Id");

        c  = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        update = (Button)findViewById(R.id.submit_btn);
        update.setText("Update");
        delete = (Button)findViewById(R.id.delete_btn);
        delete.setVisibility(View.VISIBLE);
        delete.setText("Delete");
        set_first_Date = (Button) findViewById(R.id.pick_start_date);
        set_last_Date = (Button) findViewById(R.id.pick_last_date);

        start_date = (AutoCompleteTextView) findViewById(R.id.medication_start_date);
        end_date = (AutoCompleteTextView) findViewById(R.id.medication_end_date);
        name = (AutoCompleteTextView) findViewById(R.id.name);

        start_date.setText(getIntent().getStringExtra("start"));
        end_date.setText(getIntent().getStringExtra("end"));
        name.setText(getIntent().getStringExtra("name"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.updateMedication(new Medication(Integer.parseInt(medication_id),App.getInstance().getUserId(),name.getText().toString(),start_date.getText().toString()  ,end_date.getText().toString()));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = helper.deleteMedication(Integer.parseInt(medication_id));
                if (res > 0){
                    finish();
                }

            }
        });

        set_first_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(UpdateMedication.this,
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
                                start_date.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
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
                DatePickerDialog dpd = new DatePickerDialog(UpdateMedication.this,
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
                                end_date.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                //dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });


        // Doses

        dosesListView = (ListView) findViewById(R.id.doses_list);
        dosesListView.setTranscriptMode(0);
        helper = App.getInstance().getDbHelper();
        doses = helper.getDoses(medication_id);

        add_dose = (Button)findViewById(R.id.add_dose);

            add_dose.setVisibility(View.VISIBLE);

        add_dose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(UpdateMedication.this, AddDose.class);
                n.putExtra("medication_id",medication_id);
                UpdateMedication.this.startActivity(n);
            }
        });


        // Defined Array values to show in ListView
        values = new ArrayList<>();

        for(int i = 0; i<doses.size();i++){
            values.add(" Dose # "+ (i+1)+" " +doses.get(i).getTime());
        }

        adapter = new ArrayAdapter<String>(this,
                R.layout.centered_list_view_item, android.R.id.text1, values);

        // Assign adapter to ListView
        dosesListView.setAdapter(adapter);

        dosesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String  itemValue    = (String) dosesListView.getItemAtPosition(position);
                Intent n = new Intent(UpdateMedication.this, UpdateDose.class);
                n.putExtra("Id",""+doses.get(position).getId());
                n.putExtra("time",""+doses.get(position).getTime());
                UpdateMedication.this.startActivity(n);
            }
        });


    }
    @Override
    protected void onRestart() {
        super.onRestart();
        doses = helper.getDoses(medication_id);
        values.clear();
        for(int i = 0; i<doses.size();i++){
            values.add(" Dose # "+ (i+1)+" " +doses.get(i).getTime());
        }
        adapter.notifyDataSetChanged();
    }
}
