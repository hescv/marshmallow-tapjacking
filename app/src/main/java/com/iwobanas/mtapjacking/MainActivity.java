package com.iwobanas.mtapjacking;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.content.Intent.ACTION_SENDTO;
import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends Activity {

    private static final int PERMISSIONS_REQUEST_CODE = 1;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startOverlayService("WORM_UP");

        Button button = (Button) findViewById(R.id.button);
        Button button1 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOverlayService("SHOW");
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_CODE);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Transaction Success!", Toast.LENGTH_LONG).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Note: if we inline the methods below. QARK wont find the pending intent exploit.
                insecurePendingIntent();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Note: it seems that QARK is not finding this as an exploit. Probably sth wrong
                //      in findBroadcast.py
                insecureBroadcast();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        startOverlayServiceDelayed("HIDE");

        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showContacts();
        }
    }

    private void insecurePendingIntent() {
        Intent intent = new Intent("ACTION_FOR_MY_APP", Uri.parse("INFORM_TODO: Transfer money to user: ECE458_demo"));
        intent.setType("text/plain");
        intent.putExtra("Amount", "1000CAD");
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 1, intent, 0);
        // call the pendingintent in two seconds
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, pendingIntent);
    }

    private void insecureBroadcast() {
        Uri uri = Uri.parse("https://mybank.com/login?user=ece458&password=123321");
        Intent intentBrod = new Intent(ACTION_VIEW, uri);
        //intentBrod.setAction("com.bankapp.ShowCCInfo");
        intentBrod.putExtra("CreditCard", "517517517517517");
        //sendBroadcast(intentBrod);
        startActivity(intentBrod);
    }

    private void startOverlayService(String action) {
        Intent intent = new Intent(action);
        intent.setClassName("com.iwobanas.mtapjacking.service", "com.iwobanas.mtapjacking.service.OverlayService");
        //intent.setClassName("com.iwobanas.mtapjacking", "com.iwobanas.mtapjacking.TapjackService");
        startService(intent);
    }

    private void showContacts() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.contactListContainer, new ContactListFragment())
                .commit();
    }

    private void startOverlayServiceDelayed(final String action) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startOverlayService(action);
            }
        }, 100);
    }
}
