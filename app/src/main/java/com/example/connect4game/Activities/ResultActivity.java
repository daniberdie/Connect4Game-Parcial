package com.example.connect4game.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect4game.Classes.SQLite;
import com.example.connect4game.Globals;
import com.example.connect4game.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Formatter;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSendResults, btnNewGame, btnExitGame;
    private int size, finalcells;
    private long time;
    private boolean withTime;
    private String alias, result, timeUsedOrRemaining, detail_result;

    private Toolbar toolbar;

    private EditText editText_date;
    private EditText editText_log;
    private EditText editText_email;

    private ImageView resultLandImage;
    private SQLite sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this,R.xml.config_preferences, false);

        resultLandImage = findViewById(R.id.imageResult);

        editText_date = findViewById(R.id.editText_date);
        editText_log = findViewById(R.id.editText_log);
        editText_email = findViewById(R.id.editText_email);

        btnSendResults = findViewById(R.id.btnSendResults);
        btnNewGame = findViewById(R.id.btnNewGame);
        btnExitGame = findViewById(R.id.btnExitGame);

        btnSendResults.setOnClickListener(this);
        btnNewGame.setOnClickListener(this);
        btnExitGame.setOnClickListener(this);

        if (savedInstanceState != null) {
            recuperateInstances(savedInstanceState);
        } else {
            Intent intent = getIntent();
            getIntentValues(intent);
            //Mostrar en el EditText del Log 'Temps restant' o 'Durada partida' en cas de haver-hi control de temps o no respectivament
            if(withTime)timeUsedOrRemaining = " Temps restant : ";
            else timeUsedOrRemaining = " Durada partida : ";
            setEditTexts();
        }

        //Se mostrarà imatges de Victoria, Derrota o Empat quan la pantalla està orientada horitzontalment
        setImageResult();

        saveToDataBase();
    }

    private void saveToDataBase() {
        sqlite = new SQLite(this);
        sqlite.addResult(alias, editText_date.getText().toString(), size, withTime, time, result, detail_result);
    }

    private void setImageResult() {
        if(result.equals(getString(R.string.victory_result))){
            resultLandImage.setImageResource(R.drawable.win);
        }else if (result.equals(getString(R.string.defeat_result))){
            resultLandImage.setImageResource(R.drawable.lose);
        }else{
            resultLandImage.setImageResource(R.drawable.draw);
        }
    }

    private void getIntentValues(Intent intent) {
        size = intent.getIntExtra(Globals.SIZE, 0);
        withTime = intent.getBooleanExtra(Globals.TIME_CONTROL, false);
        time = intent.getLongExtra(Globals.TIME_LEFT, 40);
        alias = intent.getStringExtra(Globals.ALIAS);
        finalcells = intent.getIntExtra(Globals.CELLS, 0);
        result = intent.getStringExtra(Globals.RESULT);
        detail_result = intent.getStringExtra(Globals.DETAIL_RESULT);
    }

    private void recuperateInstances(Bundle savedInstanceState) {
        editText_date.setText(savedInstanceState.getString(Globals.ResultDate));
        editText_log.setText(savedInstanceState.getString(Globals.ResultLog));
        editText_email.setText(savedInstanceState.getString(Globals.ResultEmail));
        size = savedInstanceState.getInt(Globals.SIZE, 0);
        withTime = savedInstanceState.getBoolean(Globals.TIME_CONTROL);
        time = savedInstanceState.getLong(Globals.TIME_LEFT, 0);
        alias = savedInstanceState.getString(Globals.ALIAS);
        result = savedInstanceState.getString(Globals.RESULT);
        detail_result = savedInstanceState.getString(Globals.DETAIL_RESULT);

    }

    private void setEditTexts() {
        Date date = new Date();
        editText_date.setText(" " + android.text.format.DateFormat.format("dd/MM/yyyy kk:mm",date.getTime()));
        createLog();
    }

    private void createLog() {
        String controledTime = withTime ? getString(R.string.timeActived) : getString(R.string.timeDisabled);
        String moreLog = " Alias: " + alias + "\n" +
                         " Mida graella: " + size + "\n" +
                         " " + controledTime + " " + "\n" +
                         timeUsedOrRemaining + time + " "  + getString(R.string.seconds) + "\n" +
                         " Caselles restants: " + finalcells + "\n" +
                         " Resultat: " + result;
        editText_log.setText(moreLog);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSendResults:
                sendEmailWithResults();
                break;
            case R.id.btnNewGame:
                startNewGame();
                break;
            case R.id.btnExitGame:
                finish();
                break;
        }

    }

    private void sendEmailWithResults() {
        String [] mail = {editText_email.getText().toString().trim()};
        if(mail[0].isEmpty()){
            Toast.makeText(this, R.string.email_error, Toast.LENGTH_LONG).show();
        } else {
            //Obrir el correu electrònic amb el missatge preparat per a ser enviat.
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/email");
            intent.putExtra(Intent.EXTRA_EMAIL, mail);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Resultats partida Connect4 " + editText_date.getText());
            intent.putExtra(Intent.EXTRA_TEXT, editText_log.getText());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

            startActivity(intent);
        }
    }

    private void startNewGame() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ResultActivity.this);
        String player = prefs.getString(getResources().getString(R.string.user_key),
                getResources().getString(R.string.user_default));
        if(player.equals("")){
            Toast.makeText(ResultActivity.this, R.string.toast_message, Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(ResultActivity.this, GameActivity.class);
            intent.putExtra(Globals.ALIAS,player);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Globals.ResultDate, editText_date.getText().toString());
        outState.putString(Globals.ResultLog, editText_log.getText().toString());
        outState.putString(Globals.ResultEmail, editText_email.getText().toString());
        outState.putInt(Globals.SIZE, size);
        outState.putBoolean(Globals.TIME_CONTROL, withTime);
        outState.putLong(Globals.TIME_LEFT, time);
        outState.putString(Globals.ALIAS, alias);
        outState.putString(Globals.RESULT, result);
        outState.putString(Globals.DETAIL_RESULT,detail_result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.menu_preferences);

        //Mostrar opció de menú per tornar a la pantalla de inici
        MenuItem item_main = menu.findItem(R.id.main_option);
        item_main.setVisible(true);
        toolbar.setTitle("");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.preference_option:
                        Intent intent_pref = new Intent(ResultActivity.this, PreferencesActivity.class);
                        startActivity(intent_pref);
                        return true;
                    case R.id.main_option:
                        Intent intent_main = new Intent(ResultActivity.this, MainActivity.class);
                        startActivity(intent_main);
                        finish();
                        return true;
                    default:
                        return ResultActivity.super.onOptionsItemSelected(item);
                }
            }
        });
        return true;
    }
}
