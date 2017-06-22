package com.projectandroid.kevinjimezb.appcrtour.Utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.projectandroid.kevinjimezb.appcrtour.CRMap;

/**
 * Created by Keyla Espinoza on 09/04/2015.
 */
public class Util_GPS extends Service implements LocationListener {

    private final Context context;
    private Activity activity;
    private boolean isGPSEnabled = false; //GPS status
    private boolean canGetLocation = false; //GPS status
    private Location currentLocation;
    private double currentLatitude;
    private double currentLongitude;
    private double currentAltitude;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_FOR_UPDATE = 5; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 3000;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public Util_GPS(CRMap pActivity) {

        this.activity = pActivity;
        this.context = pActivity.getApplicationContext();

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATE, this);
        }

        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            getCurrentLocation();
        } else {
            new MaterialDialog.Builder(activity)
                    .title("gps is disabled in your device enable it")
                    .positiveText("Enable")
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(callGPSSettingIntent);
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            materialDialog.hide();
                        }
                    })
                    .show();
        }

    }

    public Location getCurrentLocation() {
        try {

            if (isGPSEnabled) {

                if (locationManager != null) {

                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATE, this);
                    }

                    if (currentLocation != null) {
                        this.canGetLocation = true;
                        currentAltitude = currentLocation.getAltitude();
                        currentLatitude = currentLocation.getLatitude();
                        currentLongitude = currentLocation.getLongitude();
                    } else {
                        this.canGetLocation = false;
                    }
                } else {
                    locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATE, this);
                    }

                    this.canGetLocation = false;
                }

            } else {
                this.canGetLocation = false;
                currentLocation = null;
            }

        } catch (Exception e) {
            currentLocation = null;
            return currentLocation;
        }

        return currentLocation;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(Util_GPS.this);
        }
    }

    /**
     * Function to get currentLatitude
     */
    public double getCurrentLatitude() {
        if (currentLocation != null) {
            currentLatitude = currentLocation.getLatitude();
        }

        // return currentLatitude
        return currentLatitude;
    }

    /**
     * Function to get currentLongitude
     */
    public double getCurrentLongitude() {
        if (currentLocation != null) {
            currentLongitude = currentLocation.getLongitude();
        }
        return currentLongitude;
    }

    /**
     * Function to get currentAltitude
     */
    public double getCurrentAltitude() {
        if (currentLocation != null) {
            currentAltitude = currentLocation.getAltitude();
        }
        return currentAltitude;
    }

    public boolean canGetLocation() {

        if (!this.canGetLocation) {
            Toast toast = Toast.makeText(context, "Can't get GPS signal", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            return this.canGetLocation;
        } else {
            return this.canGetLocation;
        }

    }

    public boolean canGetLocationWithOutToast() {
            return this.canGetLocation;
    }

    public void showSettingsAlert() {

        Toast toast = Toast.makeText(context,"the location was not obtained", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            currentLocation = location;
            this.canGetLocation = true;
        } else {
            this.canGetLocation = false;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}