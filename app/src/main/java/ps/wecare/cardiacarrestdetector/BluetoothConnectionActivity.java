package ps.wecare.cardiacarrestdetector;


import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import ps.wecare.cardiacarrestdetector.db.Message;
import ps.wecare.cardiacarrestdetector.db.myDbAdapter;


public class BluetoothConnectionActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    final String ON = "O";
    final String OFF = "F";

    BluetoothSPP bluetooth;

    private Button connect;
    Button on;
    Button off;

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series ;

    private int lastX = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bluetooth_connection);

        setContentView(R.layout.activity_main);

        // set User name in header
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.header_name);
        navUsername.setText(App.getInstance().getSharedPreferences().getString(App.NAME,"Anonymous"));

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        connect = (Button)findViewById(R.id.connect_btn);
        on = (Button) findViewById(R.id.on);
        off = (Button) findViewById(R.id.off);
        // we get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
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
            }

            public void onDeviceDisconnected() {
                connect.setText("Connection lost");
            }

            public void onDeviceConnectionFailed() {
                connect.setText("Unable to connect");
            }
        });
        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                //Toast.makeText(BluetoothConnectionActivity.this,  message , Toast.LENGTH_LONG).show();
                //series.appendData(new DataPoint(lastX++ , Integer.parseInt(message)), true, 100);
                series.appendData(new DataPoint(lastX++ , Double.parseDouble(message)), true, 100);
                bluetooth.send(ON, true);
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
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(ON, true);
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.send(OFF, true);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_logout) {
            Message.message(this,"Logout");
            App.getInstance().logOut();
            Intent n = new Intent(BluetoothConnectionActivity.this, LoginActivity.class);
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
        // we're going to simulate real time with thread that append data to the graph
       /*
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                while(true) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
        */
    }
    // add random data to graph
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++ , RANDOM.nextDouble() * 10d), true, 100);
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
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }




}
