package com.example.connect4game.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.connect4game.R;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        button_back = findViewById(R.id.btnBack);
        button_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                returnToMainActivity();
        }
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(HelpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
