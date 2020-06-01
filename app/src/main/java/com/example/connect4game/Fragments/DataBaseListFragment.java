package com.example.connect4game.Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.connect4game.Classes.SQLite;
import com.example.connect4game.R;

import java.util.ArrayList;
import java.util.List;

public class DataBaseListFragment extends Fragment {

    SQLite sqLite;
    private ListView listView;
    private GameListener gameListener;
    private DataBaseAdapter dataBaseAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_base_list, container, false);
        sqLite = new SQLite(getActivity());
        listView = (ListView) view.findViewById(R.id.gamesListDatabase);
        dataBaseAdapter = new DataBaseAdapter(getActivity(), sqLite);
        listView.setAdapter(dataBaseAdapter);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(gameListener != null){
                    gameListener.onGameSelected(position);
                }
            }
        });

        dataBaseAdapter.notifyDataSetChanged();
    }

    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }

    public interface GameListener {
        void onGameSelected(int pos);
    }

    private class DataBaseAdapter extends BaseAdapter {
        Activity context;
        SQLite database;

        DataBaseAdapter(Context context, SQLite sqLite) {
            this.context = (Activity) context;
            this.database = sqLite;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getCount() {
            int count = database.getResult().getCount();
            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.list_item, null);

            ImageView resultImage = item.findViewById(R.id.imageListItem);

            Cursor cursor = database.getResult();
            cursor.moveToPosition(position);

            String game_result = cursor.getString(6);

            switch (game_result){
                case "Victoria":
                    resultImage.setImageResource(R.drawable.win);
                    resultImage.setBackgroundColor(getResources().getColor(R.color.colorWin));
                    break;
                case "Derrota":
                    resultImage.setImageResource(R.drawable.lose);
                    resultImage.setBackgroundColor(getResources().getColor(R.color.colorLose));
                    break;
                case "Empat":
                    resultImage.setImageResource(R.drawable.draw);
                    resultImage.setBackgroundColor(getResources().getColor(R.color.colorDraw));
                    break;
            }

            TextView alias = (TextView) item.findViewById(R.id.DBAlias);
            alias.setText(cursor.getString(1));

            TextView result = (TextView) item.findViewById(R.id.DBResult);
            //Versió 1 --> Ocultar text resultat
            //result.setText(game_result);

            TextView date = (TextView) item.findViewById(R.id.DBDate);
            date.setText(cursor.getString(2));

            return (item);
        }
    }
}
