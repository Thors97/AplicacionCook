package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Handler para pasar del MainActivity al PrincipalActivity
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this, PrincipalActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 800); //0.8 segs
    }

}
