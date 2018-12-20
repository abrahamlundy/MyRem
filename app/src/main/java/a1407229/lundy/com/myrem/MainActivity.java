package a1407229.lundy.com.myrem;

//Lundy Van Kevin
// 1407229

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.instantapps.ActivityCompat;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements SensorEventListener,GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{
    ArrayList<Data> alData = new ArrayList<>();
    double ax=0,ay=0,az=0;
    RecyclerView rvData;
    RecyclerView.LayoutManager lm;
    double lat,lon;
    Task<Location> mLastLocation;
    GoogleApiClient mGoogleClient;
    Toolbar myToolbar;
//    DbSensor dbSensor;
    TextView tvHasil;


    private SensorManager sm;
    private Sensor senAccel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHasil = (TextView) findViewById(R.id.tvHasil);

        //
        alData.add(new Data("1data1", "1data2","1data3"));
        alData.add(new Data("2data1", "2data2","2data3"));

        rvData =  (RecyclerView) findViewById(R.id.rvData);
        rvData.setHasFixedSize(true);

        //adapter
        AdapterData adapter = new AdapterData(alData);
        rvData.setAdapter(adapter);

        //layout manager
        lm = new LinearLayoutManager(this);
        rvData.setLayoutManager(lm);

        //supaya ada garis vertikal
        rvData.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


  //     Toolbar myToolbar =  findViewById(R.id.toolbar);
//       setSupportActionBar(myToolbar);


        //cek apakah sensor tersedia
        sm = (SensorManager)    getSystemService(getApplicationContext().SENSOR_SERVICE);
        senAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (senAccel != null){
            // ada sensor accelerometer!
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setMessage("Sukses, device punya sensor accelerometer!");
            ad.show();
        }
        else {
            // gagal, tidak ada sensor accelerometer.
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setMessage("Tidak ada sensor accelerometer!");
            ad.show();
        }

        buildGoogleApiClient();
    }

    //Membuat API Client
    protected synchronized void buildGoogleApiClient(){
        mGoogleClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
/*
    public void AmbilLokasi(){

        mLastLocation = LocationServices.getFusedLocationProviderClient(this).getLastLocation();​
        Toast.makeText(this,String.valueOf(mLastLocation.getResult()),Toast.LENGTH_SHORT);
    }
*/
    public void  simpan() {
        OpenHelper dbHelper =  new OpenHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("DATA1", ax);
        newValues.put("DATA2", ay);
        newValues.put("DATA3", az);
        db.insert("DATA", null, newValues);
        db.close();
    }

    public String load() {
        Cursor cur = null;
        OpenHelper dbHelper =  new OpenHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] cols = new String [] {"DATA1", "DATA2","DATA3"};
        cur = db.query("DATA",cols,null,null,null,null,null);
        String gab = "";
        if (cur.getCount()>0) {  //ada data? ambil
            cur.moveToFirst();
            int data1 = cur.getInt(0);
            int data2 = cur.getInt(1);
            int data3 = cur.getInt(2);
            gab = Integer.toString(data1)+":"+Integer.toString(data2)+":"+Integer.toString(data3);
        }
        db.close();
        return gab;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu1:
                Toast.makeText(getApplicationContext(), "Simpan", Toast.LENGTH_LONG).show();
                simpan();

                return true;
            case R.id.menu2:
                String hasil = load();

                Toast.makeText(getApplicationContext(), "Load:"+hasil, Toast.LENGTH_LONG).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            ax=sensorEvent.values[0];
            ay=sensorEvent.values[1];
            az=sensorEvent.values[2];
        }
        tvHasil.setText("x:"+ax+" y:"+ay+" z:"+az);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, senAccel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
