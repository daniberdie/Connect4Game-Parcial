package com.example.connect4game.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.connect4game.Fragments.DataBaseListFragment;
import com.example.connect4game.Fragments.DetailFragment;
import com.example.connect4game.Fragments.ResultPFragment;
import com.example.connect4game.R;

public class DataBaseActivity extends FragmentActivity implements DataBaseListFragment.GameListener {

    private Button back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        back_button = findViewById(R.id.back_databaseActivity);

        DataBaseListFragment fragmentList = (DataBaseListFragment) getSupportFragmentManager().
                findFragmentById(R.id.ListFrag);
        fragmentList.setGameListener(this);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataBaseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onGameSelected(int pos) {
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.DetailFrag);
        if (detailFragment != null && detailFragment.isInLayout()) {
            detailFragment.viewDetails(pos);
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("position_select", pos);
            startActivity(intent);
        }

        //Versio 1 parcial
        ResultPFragment resultPFragment = (ResultPFragment) getSupportFragmentManager().findFragmentById(R.id.ResultPFrag);
        if(resultPFragment != null && resultPFragment.isInLayout()){
            resultPFragment.viewDetails(pos);
        }
    }
}
