package com.example.connect4game.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.connect4game.Fragments.DetailFragment;
import com.example.connect4game.R;

public class DetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DetailFragment detalle = (DetailFragment) getSupportFragmentManager().
                findFragmentById(R.id.DetailFrag);
        detalle.viewDetails(getIntent().getIntExtra("position_select", 0));
    }
}
