package ps.wecare.cardiacarrestdetector;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import ps.wecare.cardiacarrestdetector.Beloved.AddBeloved;
import ps.wecare.cardiacarrestdetector.Beloved.BelovedList;
import ps.wecare.cardiacarrestdetector.Medications.AddMedication;
import ps.wecare.cardiacarrestdetector.Medications.MedicationsList;
import ps.wecare.cardiacarrestdetector.db.Beloved;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;


public class BluetoothConnectionActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private final String START = "O";

    BluetoothSPP bluetooth;

    private Button connect;
    private Button start;
    private TextView guide;

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series ;

    private int lastX = 0;
    private myDbAdapter helper;
    private GraphView graph;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bluetooth_connection);
        if (App.getInstance().showGuide()) {
            Intent n = new Intent(BluetoothConnectionActivity.this, GuideActivity.class);
            BluetoothConnectionActivity.this.startActivity(n);
            finish();
        }

        if (!App.getInstance().isLoggedIn()) {
            Intent n = new Intent(BluetoothConnectionActivity.this, LoginActivity.class);
            BluetoothConnectionActivity.this.startActivity(n);
            finish();
        }

        helper = App.getInstance().getDbHelper();
        ArrayList<Beloved> beloved = helper.getBeloved(App.getInstance().getUserId());
        if (beloved.size() == 0){
            Intent n = new Intent(BluetoothConnectionActivity.this, AddBeloved.class);
            BluetoothConnectionActivity.this.startActivity(n);
            finish();
        }

        setContentView(R.layout.activity_main);


        connect = (Button)findViewById(R.id.connect_btn);
        start = (Button) findViewById(R.id.start);
        guide = (TextView)findViewById(R.id.bluetooth_guide_area);
        // set User name in header
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.header_name);
        navUsername.setText(App.getInstance().getUserName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);




        // we get graph view instance
        graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        //viewport.setYAxisBoundsManual(true);
        //viewport.setMinY(0);
        //viewport.setMaxY(10);
        viewport.setScrollable(true);
        viewport.setScrollableY(true);
        viewport.setScalable(true);
        viewport.setScalableY(true);


        bluetooth = new BluetoothSPP(this);
        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }
        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                connect.setText("Connected to " + name);
                start.setVisibility(View.VISIBLE);
                graph.setVisibility(View.VISIBLE);
                guide.setVisibility(View.GONE);
            }

            public void onDeviceDisconnected() {
                connect.setText("Connection lost");
                start.setVisibility(View.GONE);
                graph.setVisibility(View.GONE);
                guide.setVisibility(View.VISIBLE);

            }

            public void onDeviceConnectionFailed() {
                connect.setText("Unable to connect");
                start.setVisibility(View.GONE);
                graph.setVisibility(View.GONE);
                guide.setVisibility(View.VISIBLE);

            }
        });
        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                //Toast.makeText(BluetoothConnectionActivity.this,  message , Toast.LENGTH_LONG).show();
                //series.appendData(new DataPoint(lastX++ , Integer.parseInt(message)), true, 100);
                series.appendData(new DataPoint(lastX++ , Double.parseDouble(message)), true, 100);
                bluetooth.send(START, true);
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetooth.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(START, true);
            }
        });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_beloved_list) {
            Intent n = new Intent(BluetoothConnectionActivity.this, BelovedList.class);
            BluetoothConnectionActivity.this.startActivity(n);
        } else if (id == R.id.nav_medications_list) {
            Intent n = new Intent(BluetoothConnectionActivity.this, MedicationsList.class);
            BluetoothConnectionActivity.this.startActivity(n);
        }else if (id == R.id.nav_logout) {
            Message.message(this,"Logout");
            App.getInstance().logOut();
            Intent n = new Intent(BluetoothConnectionActivity.this, LoginActivity.class);
            BluetoothConnectionActivity.this.startActivity(n);
            finish();
        } else if (id == R.id.nav_ar) {
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            Resources resources = getResources();
            resources.updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

            Intent n = new Intent(BluetoothConnectionActivity.this, BluetoothConnectionActivity.class);
            BluetoothConnectionActivity.this.startActivity(n);
            finish();

        } else if (id == R.id.nav_en) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            Resources resources = getResources();
            resources.updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
            Intent n = new Intent(BluetoothConnectionActivity.this, BluetoothConnectionActivity.class);
            BluetoothConnectionActivity.this.startActivity(n);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
        public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Message.message(getApplicationContext() , "Bluetooth was not enabled.");
                finish();
            }
        }
    }




}
