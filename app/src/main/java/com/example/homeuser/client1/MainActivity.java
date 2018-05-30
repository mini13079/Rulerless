package com.example.homeuser.client1;

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

public class MainActivity extends AppCompatActivity {
    EditText lat, lon, uid;
    Button insert, get_results, gps,single,user1,user0;
    TextView results, gps_result;
    RequestQueue rq;
    String update1 = "http://rulerless.net16.net/newupdate1.php";
    String update2 = "http://rulerless.net16.net/newupdate2.php";
    String getquery = "http://rulerless.net16.net/newgetquery.php";
    String getquery2 = "http://rulerless.net16.net/newgetquery2.php";
    String latitude="0";
    String longitude="0";
    String userid;
    private LocationManager locationManager;
    private LocationListener listener;
    double _lat = 0;
    double _lon = 0;
    String datestamp = new SimpleDateFormat("MM-dd-yy").format(Calendar.getInstance().getTime());
    String timestamp= new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    String received_time="0";
    String received_date="0";
    String [] receivedtimesplit;
    String [] timestampsplit;
    int user=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        gps = (Button) findViewById(R.id.gps);

        results = (TextView) findViewById(R.id.results);
        single = (Button) findViewById(R.id.single);
        user1 = (Button) findViewById(R.id.user1);
        user0 = (Button) findViewById(R.id.user0);
        gps_result = (TextView) findViewById(R.id.gps_result);
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


        rq = Volley.newRequestQueue(getApplicationContext());


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

        user1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            user=1;

            }


        });

        user0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user=0;

            }


        });

        single.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SingleUser.class);

                startActivity(intent);

            }


        });

        gps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Handler handler=new Handler();
                final Runnable updategps=new Runnable(){

                    @Override
                    public void run() {
                        single.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), SingleUser.class);

                                startActivity(intent);

                            }


                        });

                        timestamp =new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                        datestamp= new SimpleDateFormat("MM-dd-yy").format(Calendar.getInstance().getTime());
                        configure_button();
                        // get_gps();
                        if(user==0) {
                            updatelocation();
                            get_coordinates2();
                        }
                        else if(user==1){
                            updatelocation2();
                            get_coordinates();
                        }




                        if(Objects.equals(datestamp,received_date)&&timeverify()==true) {
                            gps_result.setText("Distance: " + distance(_lat, _lon, Double.parseDouble(latitude), Double.parseDouble(longitude)) + " ft");
                        }
                        else{
                            gps_result.setText("Waiting for communicating user to update coordinates");
                        }
                        results.setText("Received time: "+received_time+"\nReceived date: "+received_date+"\nReceived latitude: "+latitude+"\nRecieved longitude: "+longitude);
                        results.append("\nYour time: "+timestamp+"\nYour date: "+datestamp+"\nYour latitude: "+_lat+"\nYour longitude: "+_lon);

                        //gps_result.setText("LAT: " + _lat + " Long: " + _lon);
                        handler.postDelayed(this,1000);

                    }
                };
                handler.postDelayed(updategps,1000);
            }


        });


    }

    public void get_gps() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
//                        ,10);
//            }
//            return;
//        }

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //latitude of location
        if(myLocation!=null)
            _lat = myLocation.getLatitude();

        //longitude og location
        if(myLocation!=null)
            _lon = myLocation.getLongitude();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

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

    void updatelocation(){
        get_gps();
        StringRequest request = new StringRequest(Request.Method.POST, update1, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("lat", Double.toString(_lat));
                parameters.put("lon", Double.toString(_lon));
                parameters.put("time", timestamp);
                parameters.put("date", datestamp);



                return parameters;
            }


        };
        rq.add(request);
    }

    void updatelocation2(){
        get_gps();
        StringRequest request = new StringRequest(Request.Method.POST, update2, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("lat", Double.toString(_lat));
                parameters.put("lon", Double.toString(_lon));
                parameters.put("time", timestamp);
                parameters.put("date", datestamp);



                return parameters;
            }


        };
        rq.add(request);
    }

    void get_coordinates2(){
        JsonObjectRequest jo = new JsonObjectRequest(Request.Method.POST, getquery2, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Rulerless = response.getJSONArray("Rulerless2");
                    JSONObject Ruler = Rulerless.getJSONObject(0);
                    latitude = Ruler.getString("lat");
                    longitude = Ruler.getString("lon");
                    received_date=Ruler.getString("date");
                    received_time=Ruler.getString("time");
                    userid = Ruler.getString("uid");
                    //results.setText("latitude: " + latitude + " longitude: " + longitude + " userid:" + userid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        rq.add(jo);

    }

    void get_coordinates(){
        JsonObjectRequest jo = new JsonObjectRequest(Request.Method.POST, getquery, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Rulerless = response.getJSONArray("Rulerless2");
                    JSONObject Ruler = Rulerless.getJSONObject(0);
                    latitude = Ruler.getString("lat");
                    longitude = Ruler.getString("lon");
                    received_date=Ruler.getString("date");
                    received_time=Ruler.getString("time");
                    userid = Ruler.getString("uid");
                    //results.setText("latitude: " + latitude + " longitude: " + longitude + " userid:" + userid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        rq.add(jo);

    }

    public boolean timeverify(){
        receivedtimesplit=received_time.split(":");
        timestampsplit=timestamp.split(":");
        if(Objects.equals(receivedtimesplit[0],timestampsplit[0])&&Objects.equals(receivedtimesplit[1],timestampsplit[1])){
            return true;
        }
        else{
            return false;
        }
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
