package com.example.familysafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class GPSStatus extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final LocationManager mamager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(mamager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(context,"GPS enable",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"GPS disable",Toast.LENGTH_SHORT).show();
        }
    }
}
