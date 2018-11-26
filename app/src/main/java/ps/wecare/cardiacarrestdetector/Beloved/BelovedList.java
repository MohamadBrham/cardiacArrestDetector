package ps.wecare.cardiacarrestdetector.Beloved;

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
import ps.wecare.cardiacarrestdetector.R;
import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;

public class BelovedList extends AppCompatActivity {

    private ListView belovedList;
    private myDbAdapter helper;
    private ArrayList<String> values;
    private ArrayAdapter<String> adapter;
    private Button add;
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
            values.add(beloved.get(i).getName() + " : " + beloved.get(i).getPhone());
        }

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
        n.putExtra("name",""+beloved.get(position).getName());
        BelovedList.this.startActivity(n);
        }

    });

        add = (Button)findViewById(R.id.add_beloved);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(BelovedList.this, AddBeloved.class);
                n.putExtra("NEW","NEW");
                BelovedList.this.startActivity(n);
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
        beloved = helper.getBeloved(App.getInstance().getUserId());
        values.clear();
        for(int i = 0; i<beloved.size();i++){
            values.add(beloved.get(i).getName() + " : " + beloved.get(i).getPhone());
        }
        adapter.notifyDataSetChanged();
    }
}
