package com.example.connect4game.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.connect4game.Globals;
import com.example.connect4game.R;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnStart, btnTwoPlayers, btnBack;
    private EditText etAlias;
    private RadioGroup radioGroup;
    private RadioButton radioButtonSize;
    private CheckBox checkBoxTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //Buttons
        btnStart = findViewById(R.id.btnStart);
        btnTwoPlayers = findViewById(R.id.btnTwoPlayers);
        btnBack = findViewById(R.id.btnBack);

        btnStart.setOnClickListener(this);
        btnTwoPlayers.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //EditText
        etAlias = findViewById(R.id.alias_editText);

        //RadioGroup
        radioGroup = findViewById(R.id.radioGroup);

        //CheckBox
        checkBoxTime = findViewById(R.id.checkTime);
    }

    @Override
    public void onClick(View v) {

        radioButtonSize = findViewById(radioGroup.getCheckedRadioButtonId());

        switch(v.getId()){
            case R.id.btnStart:
                if(etAlias.getText().toString().isEmpty()){
                    Toast.makeText(this, R.string.toast_message, Toast.LENGTH_LONG).show();
                }else{
                    startGame(true);
                }
                break;

            case R.id.btnTwoPlayers:
                if(etAlias.getText().toString().isEmpty()){
                    Toast.makeText(this, R.string.toast_message, Toast.LENGTH_LONG).show();
                }else {
                    startGame(false);
                }
                break;

            case R.id.btnBack:
                moveToMainActivity();
                break;
        }

    }

    private void moveToMainActivity() {
        Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    //Passar per parametre si hi haura IA (cpu)
    private void startGame(boolean cpu) {
        Intent intent = new Intent(ConfigActivity.this, GameActivity.class);
        intent.putExtra(Globals.ALIAS, etAlias.getText().toString());
        intent.putExtra(Globals.SIZE, Integer.parseInt(radioButtonSize.getText().toString()));
        intent.putExtra(Globals.TIME_CONTROL, checkBoxTime.isChecked());
        intent.putExtra(Globals.CPU_MODE, cpu);
        finish();
        startActivity(intent);
    }
}
