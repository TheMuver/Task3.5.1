package com.example.task351;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.rtp.AudioStream;
import android.os.IBinder;
import android.widget.Toast;

import java.io.File;

public class MyService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private boolean isPlaying;
    private MediaPlayer music;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service online", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        isPlaying = false;
        onResume();
        music = MediaPlayer.create(this, R.raw.music1);
        music.setLooping(true);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onPause();
        music.stop();
        Toast.makeText(this, "Service offline",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final int type = event.sensor.getType();
        if (type == Sensor.TYPE_LIGHT){
            //darkMeasurer = event.values.clone();
            try {
                playMusic(event.values.clone()[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void playMusic(float lightMeasure) {
        if (lightMeasure < 5 && !isPlaying) {
            Toast.makeText(this, "Music started" + lightMeasure, Toast.LENGTH_SHORT).show();
            isPlaying = true;
            music.start();
        }
        else if (lightMeasure >= 5 && isPlaying){
            Toast.makeText(this, "Music stopped",Toast.LENGTH_SHORT).show();
            isPlaying = false;
            music.pause();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing
    }

    public void onResume() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void onPause() {
        sensorManager.unregisterListener(this);
    }
}
