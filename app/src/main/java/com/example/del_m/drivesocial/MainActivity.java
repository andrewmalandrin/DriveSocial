package com.example.del_m.drivesocial;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

   Button btnLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogar = (Button) findViewById(R.id.btnLogar);

    }

    public void logar (View view){
        Intent intent = new Intent(MainActivity.this, AssistentActivity.class);
        startActivity(intent);

    }






}
