package com.example.familysafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    BetteryBroadCast brd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // brd= new BetteryBroadCast();
    }

    @Override
    protected void onStart() {
       // this.registerReceiver(brd,new IntentFilter(Intent.ACTION_BATTERY_LOW));
        super.onStart();
    }
}