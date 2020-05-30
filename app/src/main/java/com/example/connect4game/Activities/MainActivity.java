package com.example.connect4game.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.connect4game.Globals;
import com.example.connect4game.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_help, button_init, button_exit, button_database;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this,R.xml.config_preferences, false);

        button_help = findViewById(R.id.btnHelp);
        button_init = findViewById(R.id.btnInit);
        button_exit = findViewById(R.id.btnExit);
        button_database = findViewById(R.id.btnDataBase);

        button_help.setOnClickListener(this);
        button_init.setOnClickListener(this);
        button_exit.setOnClickListener(this);
        button_database.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHelp:
                moveToHelpActivity();
                break;
            case R.id.btnInit:
                moveToGameActivity();
                break;
            case R.id.btnDataBase:
                moveToDataBaseActivity();
                break;
            case R.id.btnExit:
                finish();
                break;
        }

    }

    private void moveToGameActivity() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String player = prefs.getString(getResources().getString(R.string.user_key),
                getResources().getString(R.string.user_default));
        if(player.equals("")){
            Toast.makeText(MainActivity.this, R.string.toast_message, Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra(Globals.ALIAS,player);
            startActivity(intent);
            finish();
        }
    }

    private void moveToDataBaseActivity() {
        Intent intent = new Intent(MainActivity.this, DataBaseActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.menu_preferences);

        MenuItem item_main = menu.findItem(R.id.main_option);
        item_main.setVisible(false);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.preference_option:
                        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return MainActivity.super.onOptionsItemSelected(item);
                }
            }
        });
        return true;
    }
}
