package ps.wecare.cardiacarrestdetector;

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

import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class BelovedList extends AppCompatActivity {

    ListView belovedList;
    private myDbAdapter helper;
    private ArrayList<String> values;
    ArrayAdapter<String> adapter;
    private Button add;
    private int posi;
    private ArrayList<Beloved> beloved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beloved_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.centered_title_layout);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(R.string.beloved_circle_list_title);

        belovedList = (ListView) findViewById(R.id.beloved_list);

        helper = App.getInstance().getDbHelper();
        beloved = helper.getBeloved(App.getInstance().getUserId());

        // Defined Array values to show in ListView
        values = new ArrayList<>();

        for(int i = 0; i<beloved.size();i++){
            values.add(beloved.get(i).getPhone());
        }
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        adapter = new ArrayAdapter<String>(this,
                R.layout.centered_list_view_item, android.R.id.text1, values);

        // Assign adapter to ListView
        belovedList.setAdapter(adapter);

        belovedList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
        public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {

            // ListView Clicked item value
            String  itemValue    = (String) belovedList.getItemAtPosition(position);
                Intent n = new Intent(BelovedList.this, UpdateBeloved.class);
                n.putExtra("Id",""+beloved.get(position).getId());
                n.putExtra("phone",""+beloved.get(position).getPhone());
                posi = position;

                BelovedList.this.startActivity(n);
            // Show Alert
            //Message.message(getApplicationContext(),"Position :"+position+"  ListItem : " +itemValue+ "ID: "+beloved.get(position).getId());

        }

    });

        add = (Button)findViewById(R.id.add_beloved);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(BelovedList.this, BelovedCircleActivity.class);
                n.putExtra("NEW","NEW");
                BelovedList.this.startActivity(n);
            }
        });


}

    @Override
    protected void onResume() {
        super.onResume();
        //values.remove(0);
        adapter.notifyDataSetChanged();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        beloved = helper.getBeloved(App.getInstance().getUserId());


        // Defined Array values to show in ListView
        //values = new ArrayList<>();
        values.clear();
        for(int i = 0; i<beloved.size();i++){
            values.add(beloved.get(i).getPhone());
        }
        adapter.notifyDataSetChanged();
    }
}
