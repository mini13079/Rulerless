package com.example.homeuser.client1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.location.Criteria;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import android.os.Handler;
import org.json.JSONObject;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingleUser extends AppCompatActivity {

    Button marker,distance,dualuser;
    TextView result,info;


    double latitude;
    double longitude;
    private LocationManager locationManager;
    private LocationListener listener;
    double _lat = 0;
    double _lon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);




        result = (TextView) findViewById(R.id.result);
        marker = (Button) findViewById(R.id.marker);
        dualuser = (Button) findViewById(R.id.dualuser);
        distance = (Button) findViewById(R.id.distance);
        info = (TextView) findViewById(R.id.info);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                _lat=location.getLongitude();
                _lon=location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();






        // Create a criteria object to retrieve provider



//        get_results.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//                get_coordinates();
//            }
//        });
//
//        insert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updatelocation();
//            }
//        });

        dualuser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);

                startActivity(intent);

            }


        });

        distance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Handler handler=new Handler();
                final Runnable updategps=new Runnable(){

                    @Override
                    public void run() {


                        configure_button();
                        info.setText("Marker latitude: "+latitude+"\nMarker Longitude: "+longitude+"\nCurrent latitude: "+_lat+"\nCurrent Longitude: "+_lon);
                        result.setText("Distance: "+distance(_lat,_lon,latitude,longitude));
                        //gps_result.setText("LAT: " + _lat + " Long: " + _lon);
                        handler.postDelayed(this,0);

                    }
                };
                handler.postDelayed(updategps,0);
            }


        });

        marker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                configure_button();
                latitude=_lat;
                longitude=_lon;
                info.setText("Marker latitude: "+latitude+"\nMarker Longitude: "+longitude+"\nCurrent latitude: "+_lat+"\nCurrent Longitude: "+_lon);
                result.setText("Distance: "+distance(_lat,_lon,latitude,longitude));

            }


        });

    }



    public double distance(double lat1,double lon1,double lat2,double lon2){
        int R=3961;
        double dlon=Math.abs(lon2-lon1);
        double dlat=Math.abs(lat2-lat1);
        double a = Math.pow((Math.sin(dlat/2)),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow((Math.sin(dlon/2)),2);
        double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) );
        double d = R * c*5280;

        return d;



    }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.



        //noinspection MissingPermission
        locationManager.requestLocationUpdates("gps", 0, 0, listener);

    }






}