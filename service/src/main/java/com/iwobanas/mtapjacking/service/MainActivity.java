package com.iwobanas.mtapjacking.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        String content = intent.getStringExtra("Amount");
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();

        Log.i("FEFEFE", "OK");
    }

}
