package com.futurama.serenity.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.futurama.serenity.MainApplication;
import com.futurama.serenity.R;
import com.futurama.serenity.events.Position;
import com.futurama.serenity.models.User;
import com.futurama.serenity.models.WhiteList;
import com.futurama.serenity.services.ClientService;
import com.futurama.serenity.services.LocationService;
import com.futurama.serenity.services.RegistrationIntentService;
import com.futurama.serenity.utils.Config;
import com.futurama.serenity.utils.FontAwesomeIcons;
import com.futurama.serenity.utils.GenericObjectResult;
import com.futurama.serenity.utils.Session;
import com.futurama.serenity.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.joanzapata.iconify.IconDrawable;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {

    private static final String TAG = MainActivity.class.getSimpleName();

    protected LocationSettingsRequest mLocationSettingsRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    private ListView listViewSecteurActivite;

    private ProgressDialog progressDialogs;

    private GoogleCloudMessaging gcm = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String regid = null;
    private Session session;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainApplication.getBus().register(this);
        session = new Session();
        mContext = MainApplication.getInstance().getApplicationContext();

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
            gcm = GoogleCloudMessaging.getInstance(this);

            regid = getRegistrationId();
            if (!regid.isEmpty())
            {
                sendToServeur(regid);
            }
            else
            {
                registerInBackground();
                Log.d(TAG, "No valid Google Play Services APK found.");
            }
        }

        buildLocationSettingsRequest();
        subscribe();
        loadContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_actualiser).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_refresh)
                        .color(Color.parseColor("#CCCCCC"))
                        .actionBarSize());
        menu.findItem(R.id.action_sms).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_envelope)
                        .color(Color.parseColor("#FF0000"))
                        .actionBarSize());
        menu.findItem(R.id.action_config).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_cog)
                        .color(Color.parseColor("#CCCCCC"))
                        .actionBarSize());
        menu.findItem(R.id.action_quitter).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_close)
                        .color(Color.parseColor("#CCCCCC"))
                        .actionBarSize());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_actualiser :
                //TODO Actualiser
                return true;
            case R.id.action_sms :
                Intent intentSms = new Intent(mContext, SmsActivity.class);
                //intentAccueil.putExtra("action", "login");
                startActivity(intentSms);
                return true;
            case R.id.action_config :
                Intent intentSetting = new Intent(mContext, SettingsActivity.class);
                //intentAccueil.putExtra("action", "login");
                startActivity(intentSetting);
                return true;
            case R.id.action_quitter :
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Fermeture de l'application")
                        .setMessage("Voulez-vous quitter l'application?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        }).setNegativeButton("Non", null).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        float latitude = (float) location.getLatitude();
        float longitude = (float) location.getLongitude();
        session.getEditor().putFloat("latitude", latitude);
        session.getEditor().putFloat("longitude", longitude);
        session.getEditor().commit();
        MainApplication.getBus().post(new Position(latitude, longitude));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        if (mGoogleApiClient != null) {
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
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    // Implement GCM Required methods (Add below methods in LaunchActivity)

    private String getRegistrationId() {
        String registrationId = session.getSharedPref().getString("regId", "");
        if (registrationId.isEmpty()) {
            Log.d(TAG, "Registration ID not found.");
            return "";
        }
        int registeredVersion = session.getSharedPref().getInt(Config.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(mContext);
        if (registeredVersion != currentVersion) {
            Log.d(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo;
            packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Config.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Config.FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Config.DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Config.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Utils.toast("This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    /**
     * Registers the application with GCM connection servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground(){
        new AsyncTask() {
            protected Object doInBackground(Object... params)
            {
                String msg = "";
                try
                {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = gcm.register(Config.SENDER_ID);
                    session.getEditor().putString("regId", regid);
                    session.getEditor().putBoolean("SENT_REGID_TO_SERVER", true);
                    session.getEditor().commit();
                }
                catch (IOException ex)
                {
                    session.getEditor().putBoolean("SENT_REGID_TO_SERVER", false);
                    session.getEditor().commit();
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
            protected void onPostExecute(Object result)
            {
                if(regid != null && regid != ""){
                    sendToServeur(regid);
                }
            }
        }.execute(null, null, null);
    }

    private void sendToServeur(String androidId){
        Log.e("REGID", androidId);
        if(!androidId.isEmpty() && androidId.equals(session.getSharedPref().getString("androidId",""))){
            session.getEditor().putString("androidId", androidId).commit();
            Retrofit client = MainApplication.getRetrofit();
            ClientService service = client.create(ClientService.class);
            Call<GenericObjectResult<User>> call = service.updateRegistration(session.getSharedPref().getString("token", "*******************"), session.getSharedPref().getString("uid", ""), androidId);
            call.enqueue(new Callback<GenericObjectResult<User>>() {
                @Override
                public void onResponse(Response<GenericObjectResult<User>> response) {
                    if (response.isSuccess()) {
                        // request successful (status code 200, 201)
                        GenericObjectResult<User> result = response.body();
                        if (result.getRow() != null) {

                        }
                        Log.i("RegistrationID", result.toString());
                    } else {
                        //request not successful (like 400,401,403 etc)
                        //Handle errors
                        Log.i("Response code", String.valueOf(response.code()));
                        Log.i("Response message", response.message());
                        Log.i("Response header", response.headers().toString());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void subscribe(){
        if(session.getSharedPref().getString("uid","").isEmpty()){
            progressDialogs = ProgressDialog.show(this, null, "Veuillez patienter svp...", false, false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialogs.dismiss();
                }
            }, 3000);
            Retrofit client = MainApplication.getRetrofit();
            ClientService service = client.create(ClientService.class);
            Call<GenericObjectResult<User>> call = service.subscribe(session.getSharedPref().getString("token", "*******************"));
            call.enqueue(new Callback<GenericObjectResult<User>>() {
                @Override
                public void onResponse(Response<GenericObjectResult<User>> response) {
                    if (response.isSuccess()) {
                        // request successful (status code 200, 201)
                        GenericObjectResult<User> result = response.body();
                        if (result.getRow() != null) {
                            session.getEditor().putString("uid", result.getRow().getUid()).commit();
                        }
                        Log.i("RegistrationID", result.toString());
                    } else {
                        //request not successful (like 400,401,403 etc)
                        //Handle errors
                        Log.i("Response code", String.valueOf(response.code()));
                        Log.i("Response message", response.message());
                        Log.i("Response header", response.headers().toString());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        checkLocationSettings();
        if(session.getSharedPref().getBoolean("requestGPS", false)){
            //checkLocationSettings();
        }
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "Code: " + requestCode + ", Result code: "+resultCode);
        /*LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (service.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mRequestingLocationUpdates = true;
        }*/
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mRequestingLocationUpdates = true;
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        //startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.e(TAG, "All location settings are satisfied.");
                Intent intentService = new Intent(MainActivity.this, LocationService.class);
                startService(intentService);

                //startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    private void loadContacts(){
        new AsyncTask() {
            protected Object doInBackground(Object... params)
            {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
                String numero;
                while (phones.moveToNext())
                {
                    WhiteList whiteList = new WhiteList();
                    numero = Utils.cleanNumber(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    whiteList.setNumero(numero);
                    whiteList.save();
                    Log.e("Numero Open", numero);
                }
                phones.close();
                return "";
            }
            protected void onPostExecute(Object result)
            {

            }
        }.execute(null, null, null);
    }
}
