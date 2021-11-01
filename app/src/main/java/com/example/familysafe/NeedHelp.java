package com.example.familysafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NeedHelp extends AppCompatActivity implements SensorEventListener {
    Button helpButton;
    MediaPlayer mediaPlayer;
    private FirebaseAuth firebaseAuth;
    TextView x,y,z;
    SensorManager sensorManager;
    Sensor accelrometter;
    Boolean isAccelrometerAvailable;
    float currentX,currentY,currentZ,lastX,lastY,lastZ;
    Boolean itIsNotFirstTime=false;
    float xDiff,yDiff,zDiff;
    float shakeThroshold=5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help2);
        firebaseAuth=FirebaseAuth.getInstance();
        // Button
        helpButton=(Button)findViewById(R.id.help_button);

        //Declearing and intilizing navigation bar and setting its item id
        BottomNavigationView navigationView=findViewById(R.id.bottomNavView);
        navigationView.setSelectedItemId(R.id.help);

        // transition
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch (item.getItemId()){
                    case R.id.contacts:
                        startActivity(new Intent(getApplicationContext(),ContactInfo.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.location:
                        startActivity(new Intent(getApplicationContext(),MapDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.help:
                        return true;
                }
                return false;
            }
        });

        // siren functionality
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer==null){
                    mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.police_siren);
                    mediaPlayer.start();
                    helpButton.setText("Stop");
                }
                else if(mediaPlayer!=null){
                    mediaPlayer.stop();
                    mediaPlayer=null;
                    helpButton.setText("Help");
                }
            }
        });

        x= findViewById(R.id.x);
        y=findViewById(R.id.y);
        z=findViewById(R.id.z);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            accelrometter=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelrometerAvailable=true;
        }
        else{
            Toast.makeText(getApplicationContext(),"Sensor not available",Toast.LENGTH_SHORT).show();
            isAccelrometerAvailable=false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_contacts:
                Toast.makeText(this,"Add Contacts",Toast.LENGTH_SHORT).show();
                openDialogue();
                return true;
            case R.id.logout:
                checkUser();
                firebaseAuth.signOut();
                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
                
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void checkUser(){
        //check if user is already loggedIn
        //if  already loged in then open maps dashboard

        // get current user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(this,signIn.class));
            finish();
        }
        else{
            startActivity(new Intent(this,MapDashboard.class));
            finish();
        }
    }

    public void openDialogue(){

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x.setText(event.values[0]+"m/s2");
        y.setText(event.values[1]+"m/s2");
        z.setText(event.values[2]+"m/s2");
        currentX=event.values[0];
        currentY=event.values[1];
        currentZ=event.values[2];
        if(itIsNotFirstTime){
            xDiff=Math.abs(lastX-currentX);
            yDiff=Math.abs(lastY-currentY);
            zDiff=Math.abs(lastZ-currentZ);
            if((xDiff>shakeThroshold && yDiff>shakeThroshold) || (xDiff>shakeThroshold && zDiff>shakeThroshold)
                || (yDiff>shakeThroshold && zDiff>shakeThroshold)){
                Toast.makeText(getApplicationContext(),"shaked",Toast.LENGTH_SHORT).show();
                String message ="Emergency alert \n please text or call";
                String no="03173341418";
                SmsManager smsManager= SmsManager.getDefault();
                smsManager.sendTextMessage(no,null,message,null,null);
                Toast.makeText(getApplicationContext(),"msg sent",Toast.LENGTH_SHORT).show(); }
        }
        lastX=currentX; lastY=currentY; lastZ=currentZ;
        itIsNotFirstTime=true; }
    @Override
    protected void onResume() {
        super.onResume();
        if(isAccelrometerAvailable){
            sensorManager.registerListener(this,accelrometter,SensorManager.SENSOR_DELAY_NORMAL); } }
    @Override
    protected void onPause() {
        super.onPause();
        if(!isAccelrometerAvailable){
            sensorManager.unregisterListener(this); } }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}