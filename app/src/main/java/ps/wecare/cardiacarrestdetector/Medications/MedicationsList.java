package ps.wecare.cardiacarrestdetector.Medications;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ps.wecare.cardiacarrestdetector.App;
import ps.wecare.cardiacarrestdetector.Beloved.AddBeloved;
import ps.wecare.cardiacarrestdetector.Beloved.BelovedList;
import ps.wecare.cardiacarrestdetector.Beloved.UpdateBeloved;
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Medication;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class MedicationsList extends AppCompatActivity {

    private ListView MedicationsList;
    private myDbAdapter helper;
    private ArrayList<String> values;
    private ArrayAdapter<String> adapter;
    private Button add;
    private ArrayList<Medication> medications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.medications_list_title);

        MedicationsList = (ListView) findViewById(R.id.medication_list);
        helper = App.getInstance().getDbHelper();
        medications = helper.getMedications(App.getInstance().getUserId());


        // Defined Array values to show in ListView
        values = new ArrayList<>();

        for(int i = 0; i<medications.size();i++){
            values.add(medications.get(i).getName());
        }

        adapter = new ArrayAdapter<String>(this,
                R.layout.centered_list_view_item, android.R.id.text1, values);

        // Assign adapter to ListView
        MedicationsList.setAdapter(adapter);

        MedicationsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String  itemValue    = (String) MedicationsList.getItemAtPosition(position);
                Intent n = new Intent(MedicationsList.this, UpdateMedication.class);
                n.putExtra("Id",""+medications.get(position).getId());
                n.putExtra("start",""+medications.get(position).getStart());
                n.putExtra("end",""+medications.get(position).getEnd());
                n.putExtra("name",""+medications.get(position).getName());
                MedicationsList.this.startActivity(n);
            }
        });

        add = (Button)findViewById(R.id.add_medication);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(MedicationsList.this, AddMedication.class);
                MedicationsList.this.startActivity(n);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        //values.remove(0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        medications = helper.getMedications(App.getInstance().getUserId());
        values.clear();
        for(int i = 0; i<medications.size();i++){
            values.add(medications.get(i).getName());
        }
        adapter.notifyDataSetChanged();
    }



}
