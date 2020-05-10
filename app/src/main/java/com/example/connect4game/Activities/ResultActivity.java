package com.example.connect4game.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String alias, result, timeUsedOrRemaining;

    private EditText editText_date;
    private EditText editText_log;
    private EditText editText_email;

    private ImageView resultLandImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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
                moveToNewGameConfig();
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

    private void moveToNewGameConfig() {
        Intent intent = new Intent(ResultActivity.this, ConfigActivity.class);
        finish();
        startActivity(intent);
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
    }
}
