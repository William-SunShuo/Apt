package com.ss.apt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ss.aptlib.AutoCreate;

@AutoCreate
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}