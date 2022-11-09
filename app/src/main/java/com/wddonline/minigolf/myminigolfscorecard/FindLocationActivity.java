package com.wddonline.minigolf.myminigolfscorecard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindLocationActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GetDataInterface {
    private Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    ImageButton btnChooseLocation, btnPlay;
    boolean findLocation = false;
    EditText txtZipCode;
    ListView listView;
    LocationListAdapter listAdapter;
    ArrayList<LocationData> arrayList;
    ParseURL parseURL;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton btnFindLocation = findViewById(R.id.btn_find_location);
        btnFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !findLocation ) {
                    showAlert("Can not find your location. Please check your location setting and try again.");
                }
                else {
                    progress = new ProgressDialog(FindLocationActivity.this);
                    progress.setMessage("Please Wait...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setCancelable(false);
                    progress.show();

                    hideKeybaord();

                    String link = Config.root_link + "/_services/api/?service=latlng&lat=" + mLastLocation.getLatitude()+"&lng="+mLastLocation.getLongitude();
                    parseURL = new ParseURL(FindLocationActivity.this);
                    parseURL.execute(new String[]{link});
                }
            }
        });

        ImageButton btnGo = findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = new ProgressDialog(FindLocationActivity.this);
                progress.setMessage("Finding Nearby Locations...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setCancelable(false);
                progress.show();

                hideKeybaord();

                String link = Config.root_link + "/_services/api/?service=zip&zip=" + txtZipCode.getText().toString();
                parseURL = new ParseURL(FindLocationActivity.this);
                parseURL.execute(new String[]{link});
            }
        });

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        listAdapter = new LocationListAdapter(this, arrayList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Config.locationData = arrayList.get(i);
                startActivity(new Intent(FindLocationActivity.this, LocationInfoActivity.class));
            }
        });

        txtZipCode = findViewById(R.id.txt_zip);
        txtZipCode.setTypeface(Config.mainTypeFace);

        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermissions();
        }

        InitGoogleApiClient();
        ReceiveLocationUpdate();
        if (CheckPlayServices()) {

        }
    }
    void hideKeybaord(){
        View view = this.getCurrentFocus();
        if( view != null ) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    @Override
    public void onLoadedData(JSONArray resultArray) throws JSONException {
        progress.hide();
        progress.dismiss();
        arrayList = new ArrayList<>();
        if(resultArray!= null){
            for( int i = 0; i < resultArray.length(); i++){
                JSONObject object = resultArray.getJSONObject(i);
                arrayList.add(new LocationData(
                        object.getString("LOCATIONID"),
                        object.getString("STATE"),
                        object.getString("DISTANCE"),
                        object.getString("NAME"),
                        object.getString("INFOWINDOW"),
                        object.getString("TWITTER"),
                        object.getString("FACEBOOK"),
                        object.getString("WEBSITE"),
                        object.getString("ADDRESS2"),
                        object.getString("PHONE"),
                        object.getString("POSTALCODE"),
                        object.getString("ADDRESS1"),
                        object.getString("ANDROIDAPP"),
                        object.getString("LOGO"),
                        object.getString("PAGES"),
                        object.getString("IPHONEAPP"),
                        object.getString("EMAIL"),
                        object.getString("CITY"),
                        object.getString("COURSES"),
                        object.getString("PHOTO")));
            }
        }
        listAdapter.refreshArray(arrayList);
        if( arrayList.size() == 0 ) {
            showAlert("No result!");
        }
    }

    @Override
    public void onFailedLoadingData() {
        progress.hide();
        progress.dismiss();
        showAlert("Failed Loading Data");
    }

    private void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
            }
        });
        builder.show();
    }
    private boolean CheckPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 800);
            return false;
        }
        return true;
    }
    private void InitGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    private void ReceiveLocationUpdate() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("TAG","location connected");
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
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            mLastLocation = location;
            findLocation = true;
            //If everything went fine lets get latitude and longitude
            Log.d("TAG","location: "+location.getLatitude()+", "+location.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("TAG","location connected");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("TAG","Connection failed: ConnectionResult.getErrorCode");
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        findLocation = true;
    }


}
