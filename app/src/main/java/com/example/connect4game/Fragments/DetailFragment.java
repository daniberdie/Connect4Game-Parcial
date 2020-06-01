package com.example.connect4game.Fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.connect4game.Classes.SQLite;
import com.example.connect4game.Globals;
import com.example.connect4game.R;


public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }

    public void viewDetails(int cellSelected) {
        ((TextView) getView().findViewById(R.id.TxtDetail)).setText(makeText(cellSelected));
    }

    private String makeText(int position) {
        SQLite sqLite = new SQLite(getActivity());
        Cursor cursor = sqLite.getResult();
        cursor.moveToPosition(position);
        String time_control_activated;
        String game_duration;
        if(cursor.getInt(4) == 1){
            time_control_activated = "Sí";
            game_duration = "Temps restant";
        }else{
            time_control_activated = "No";
            game_duration = "Durada partida";
        }
        String textGame = "";
        textGame += getString(R.string.detail_alias) + ": " + cursor.getString(1) + '\n' +
                getString(R.string.detail_date) + ": " + cursor.getString(2) + "\n" +
                getString(R.string.detail_size) + ": " + cursor.getString(3) + "\n" +
                getString(R.string.detail_control) + ": " + time_control_activated + "\n" +
                game_duration + ": " + cursor.getString(5) + " " + getString(R.string.seconds) + "\n";
                //Versió 1 --> Ocultar text resultat per a que no surtí quan s'executí el exàmen. Per a la pràctica si ha de sortir
                //getString(R.string.detail_result)+ ": " + cursor.getString(7);
        return textGame;
    }
}
