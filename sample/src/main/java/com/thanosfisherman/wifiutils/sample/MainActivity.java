package com.thanosfisherman.wifiutils.sample;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.thanosfisherman.wifiutils.WifiUtils;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 555);
        final Button button = findViewById(R.id.button);
        WifiUtils.enableLog(true);
        //TODO: CHECK IF LOCATION SERVICES ARE ON
        button.setOnClickListener(v -> getScanResult());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void connectWithWps() {
        WifiUtils.withContext(getApplicationContext()).connectWithWps("d8:74:95:e6:f5:f8", "51362485").onConnectionWpsResult(this::checkResult).start();
    }

    private void getScanResult(){
        WifiUtils.withContext(getApplicationContext()).scanWifi(this::getScanResults).start();
    }

    private void getScanResults(@NonNull final List<ScanResult> results)
    {
        final String TAG = "MainActivity";
        if (results.isEmpty())
        {
            Log.i(TAG, "SCAN RESULTS IT'S EMPTY");
            return;
        }
        Log.i(TAG, "GOT SCAN RESULTS " + results);

        ArrayList mobileArrayObj = new ArrayList();
        HashSet hs = new HashSet();
        for (int i=0;i<results.size();i++){
            if(!results.get(i).SSID.isEmpty()){
                mobileArrayObj.add(results.get(i).SSID);
            }
        }

        hs.addAll(mobileArrayObj);
        mobileArrayObj.clear();
        mobileArrayObj.addAll(hs);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list, mobileArrayObj);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

    }

    private void connectWithWpa() {
        String ote = "conn-x828678";
        String otePass = "146080828678";

        WifiUtils.withContext(getApplicationContext())
                .connectWith(ote, otePass)
                .setTimeout(40000)
                .onConnectionResult(this::checkResult)
                .start();
    }

    private void enableWifi() {
        WifiUtils.withContext(getApplicationContext()).enableWifi(this::checkResult);
        //or without the callback
        //WifiUtils.withContext(getApplicationContext()).enableWifi();
    }

    private void checkResult(boolean isSuccess) {
        if (isSuccess)
            Toast.makeText(MainActivity.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this, "EPIC FAIL!", Toast.LENGTH_SHORT).show();
    }
}
