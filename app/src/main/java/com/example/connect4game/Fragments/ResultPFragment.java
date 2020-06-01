package com.example.connect4game.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connect4game.Classes.SQLite;
import com.example.connect4game.R;

//Versió 1 i Versió 2
public class ResultPFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_result_p_fragment, container, false);

    }

    public void viewDetails(int cellSelected) {
        //Versió 1 exàmen android
        ((ImageView) getView().findViewById(R.id.image_p_result)).setImageResource(makeImage(cellSelected));

        //Versió 2 exàmen android
        //makeImageSecondVersion(cellSelected);
    }

    //Versió 1 exàmen android
    private int makeImage(int position) {
        SQLite sqLite = new SQLite(getActivity());
        Cursor cursor = sqLite.getResult();
        cursor.moveToPosition(position);

        String result = cursor.getString(7);

        if(result.equals(getString(R.string.win))){
            return R.drawable.victoria;
        }else if (result.equals(getString(R.string.lose))){
            return R.drawable.derrota;
        }else if (result.equals(getString(R.string.draw_time))){
            return R.drawable.tiempoagotado;
        }else{
            return R.drawable.empate;
        }
    }

    //Versió 2 exàmen android
    private void makeImageSecondVersion(int position) {
        SQLite sqLite = new SQLite(getActivity());
        Cursor cursor = sqLite.getResultForVersionTwo();
        cursor.moveToPosition(position);

        byte[] image = cursor.getBlob(6);
        Bitmap icon =  BitmapFactory.decodeByteArray(image, 0, image.length);
        ((ImageView) getView().findViewById(R.id.image_p_result)).setImageBitmap(icon);
    }

}
