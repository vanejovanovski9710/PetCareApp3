package com.example.petcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button but1,but2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but1 = findViewById(R.id.btnLogin);
        but2 = findViewById(R.id.btnSignUp);

        but1.setOnClickListener(this);
        but2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignUp) {
            Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.btnLogin) {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
}