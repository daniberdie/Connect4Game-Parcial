package com.example.connect4game.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.connect4game.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_help, button_init, button_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_help = findViewById(R.id.btnHelp);
        button_init = findViewById(R.id.btnInit);
        button_exit = findViewById(R.id.btnExit);

        button_help.setOnClickListener(this);
        button_init.setOnClickListener(this);
        button_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHelp:
                moveToHelpActivity();
                break;
            case R.id.btnInit:
                moveToConfigActivity();
                break;
            case R.id.btnExit:
                finish();
                break;
        }

    }

    private void moveToConfigActivity() {
        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToHelpActivity() {
        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent);
        finish();
    }
}
