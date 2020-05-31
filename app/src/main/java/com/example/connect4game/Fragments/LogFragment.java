package com.example.connect4game.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.connect4game.R;

public class LogFragment extends Fragment {
    String text;

    @Override
    public void onCreate(Bundle savedInsanceState){
        super.onCreate(savedInsanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInsatanceState){
        return inflater.inflate(R.layout.fragment_log, container, false);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public void showLog(String log){
        TextView textView = getView().findViewById(R.id.text_log);
        text = textView.getText().toString();

        if(text.equals("")){
            text = log;
        }else{
            text = text + "\n" + log;
        }

        textView.setText(text);
    }
}
