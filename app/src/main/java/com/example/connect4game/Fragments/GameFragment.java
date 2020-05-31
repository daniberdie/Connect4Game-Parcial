package com.example.connect4game.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Parcelable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect4game.Activities.GameActivity;
import com.example.connect4game.Activities.ResultActivity;
import com.example.connect4game.Classes.Game;
import com.example.connect4game.Classes.Position;
import com.example.connect4game.Classes.Status;
import com.example.connect4game.Classes.TimeCounter;
import com.example.connect4game.Globals;
import com.example.connect4game.R;

import java.util.ArrayList;
import java.util.Random;

public class GameFragment extends Fragment {

    private int grid_size;
    private boolean time_control;
    private String player, result, detail_result;
    public long counter = 40, time_left = 0;
    private boolean multiplayer_mode;
    private int cells_left, movement_num = 0;

    private TimeCounter timeCounter;

    private GridView board;
    private ImageAdapter imageAdapter;

    private TextView turn, time, cells;

    private static final int TO_WIN  = 4;

    private Game game;

    int[] graella = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    ArrayList<Integer> availablePositions;

    private Vibrator vibrator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //S'utilitzarà el vibrador en cas d'escollir una casella incorrecta i al acabar la partida
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        getPreferences();

        if (savedInstanceState != null){getBackState(savedInstanceState);}
        else game = new Game(grid_size, TO_WIN);

        //Inicialitzar el array de IDs amb la mida del taular i afegir el drawable de casella buida a totes les caselles
        initGridDrawable();

        cells_left = grid_size * grid_size;

        board = getView().findViewById(R.id.board);
        board.setNumColumns(grid_size);

        turn = getView().findViewById(R.id.turn);
        time = getView().findViewById(R.id.timing);
        cells = getView().findViewById(R.id.cells_left);

        imageAdapter = new ImageAdapter(getActivity());
        board.setAdapter(imageAdapter);

        //Funció per ficar al TextView del torn el usuari corresponent
        setTurnText();
        //Funció per ficar al TextView de les caselles restant el número corresponent
        setCellsLeft();
        //Funció per afegir al array de IDs dels drawables, les caselles disponibles per fer una tirada
        setAvailableMovements();
        //Inicialitzar el control de temps
        setTimeControl();

        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, final int position, long id) {
                if (game.isFinished()) return;
                int col = position % grid_size;
                //Comprobar que la columna no està plena i que s'ha clickat en una de les possibles caselles
                if (game.canPlayColumn(col) && availablePositions.contains(position)){
                    //Millora: Encapsular codi
                    if(game.getStatus() == Status.PLAYER1_PLAYS){
                        setTheDrawableGrid(col,R.drawable.user_cell);
                    } else {
                        setTheDrawableGrid(col,R.drawable.oponent_cell);
                    }
                    updateTheGame();

                    if(!multiplayer_mode) {
                        if (game.isFinished()) return;
                        while (true) {
                            Random r = new Random();
                            int posCPU = r.nextInt(grid_size - 0);
                            int colCPU = posCPU % grid_size;
                            if (game.canPlayColumn(colCPU)) {
                                //Millora: Encapsular codi
                                setTheDrawableGrid(colCPU,R.drawable.oponent_cell);
                                updateTheGame();
                                break;
                            }
                        }
                    }
                }else{
                    throwInvalidMoveToast();
                }
                setAvailableMovements();
                //Actualitzar el temps després de cada jugada (Han tirat els dos jugadors).
                updateTime();

            }
        });

        //Millora: ús de notifyDataSetChange
        imageAdapter.notifyDataSetChanged();
    }

    private void updateLog(String text) {
        LogFragment logFragment = (LogFragment) getFragmentManager().findFragmentById(R.id.fragmentLog);
        if (logFragment != null && logFragment.isInLayout()){
            logFragment.showLog(text);
        }
    }

    private void getPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        player = getActivity().getIntent().getStringExtra(Globals.ALIAS);
        time_control = prefs.getBoolean(getResources().getString(R.string.time_key), true);
        multiplayer_mode = prefs.getBoolean(getResources().getString(R.string.multiplayer_key), false);
        counter = Integer.valueOf(prefs.getString(getResources().getString(R.string.selectTime_key),
                "60"));
        grid_size = Integer.valueOf(prefs.getString(getResources().getString(R.string.board_key),
                getResources().getString(R.string.board_Default_key)));
    }

    //Millora: Encapsular codi
    public void updateTheGame(){
        board.setAdapter(imageAdapter);
        cells_left--;
        setCellsLeft();
        checkFinish();
        setTurnText();
    }

    //Millora: Encapsular codi
    public void setTheDrawableGrid(int col, int drawable_id){
        Position movedPosition = game.drop(col);
        int row= movedPosition.getRow();
        int position = row * grid_size + col;
        graella[position] = drawable_id;
        movement_num++;

        //Modificar Log
        String log_text = turn.getText().toString() +": "+ "(" + "Columna: " + (col+1)+" | "+ "Fila: " + (row+1) + ")" + " Tirada: " + movement_num;
        updateLog(log_text);
    }

    public void updateTime() {
        if (time_control) {
            time_left = timeCounter.getTime() / Globals.miliSec;
            time.setText(String.valueOf(time_left) + " " + getString(R.string.seconds));
            time.setTextColor(this.getResources().getColor(R.color.colorRedLight));
        } else {
            time_left = (System.currentTimeMillis() / Globals.miliSec) - counter;
            time.setText(String.valueOf(time_left) + " " + getString(R.string.seconds));
            time.setTextColor(this.getResources().getColor(R.color.colorBlueLight));
        }
    }

    private void setTimeControl() {
        if (time_control) {
            timeCounter = new TimeCounter(counter * Globals.miliSec , Globals.miliSec, game, GameFragment.this);
            timeCounter.start();
            time.setText(counter + " " + getString(R.string.seconds));
            time.setTextColor(this.getResources().getColor(R.color.colorRedLight));
        } else {
            counter = System.currentTimeMillis() / Globals.miliSec;
            time.setText( "0" + " " + getString(R.string.seconds));
            time.setTextColor(this.getResources().getColor(R.color.colorBlueLight));
        }
    }

    private void setCellsLeft() {
        cells.setText(" " + cells_left);
    }

    private void setAvailableMovements() {
        int pos;
        availablePositions = new ArrayList<>();
        for(int i = 0; i < grid_size; i++){
            pos = game.getPositionOfLastEmptyRowInColumn(i);
            if(pos > -1){
                graella[pos] = R.drawable.possible_cell;
                availablePositions.add(pos);
            }
        }

    }

    private void throwInvalidMoveToast() {
        if(vibrator.hasVibrator()){
            vibrator.vibrate(500);
        }
        Toast.makeText(getActivity().getBaseContext(), R.string.invalid_cell, Toast.LENGTH_LONG).show();
    }

    private void getBackState(Bundle savedInstanceState) {
        game = savedInstanceState.getParcelable(Globals.GAME);
        this.player = savedInstanceState.getString(Globals.ALIAS);
        this.grid_size = savedInstanceState.getInt(Globals.SIZE);
        this.time_control = savedInstanceState.getBoolean(Globals.TIME_CONTROL);
    }

    private void initGridDrawable() {
        for(int i = 0; i < grid_size*grid_size; i++){
            graella [i] = R.drawable.empty_cell;
        }
    }

    public void checkFinish() {
        if (game.isFinished() || availablePositions.size() <= 0) {
            //Vibrar
            if(vibrator.hasVibrator()) {
                long[] pattern = {0, 300, 400, 300, 400, 300, 400};
                vibrator.vibrate(pattern,-1);
            }

            //Mostrar el toast corresponent al finalitzar la partida i guardar el resultat per a ser passat a la activitat de resultats
            if (game.getStatus() == Status.PLAYER1_WINS) {
                Toast.makeText(getActivity().getBaseContext(),  R.string.win, Toast.LENGTH_LONG).show();
                detail_result = getString(R.string.win);
                result = getString(R.string.victory_result);

            } else if (game.getStatus() == Status.PLAYER2_WINS) {
                if (!multiplayer_mode){
                    Toast.makeText(getActivity().getBaseContext(),  R.string.lose, Toast.LENGTH_LONG).show();
                    detail_result = getString(R.string.lose);
                }
                else {
                    Toast.makeText(getActivity().getBaseContext(),  R.string.player2_win, Toast.LENGTH_LONG).show();
                    detail_result = getString(R.string.player2_win);
                }

                result = getString(R.string.defeat_result);

            } else if (game.getStatus() == Status.DRAW) {
                if(cells_left <= 0){
                    cells.setText(" " + 0);
                    Toast.makeText(getActivity().getBaseContext(), R.string.draw_cells, Toast.LENGTH_LONG).show();
                    detail_result = getString(R.string.draw_cells);
                }
                else if(time_control && time_left == 0) {
                    time.setText( "0" + " " + getString(R.string.seconds));
                    Toast.makeText(getActivity().getBaseContext(), R.string.draw_time, Toast.LENGTH_LONG).show();
                    detail_result = getString(R.string.draw_time);
                }
                else{
                    Toast.makeText(getActivity().getBaseContext(), R.string.draw_time, Toast.LENGTH_LONG).show();
                    detail_result = getString(R.string.draw_time);
                }
                result = getString(R.string.draw_result);
            }
            //Finalitzar contador quan acaba la partida
            if(time_control) timeCounter.finalizeCounter();

            //Mantenir la pantalla final de la partida 3 segons per observar els resultats
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToResultsActivity();
                }
            }, 3000);
        }
    }

    private void moveToResultsActivity() {
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra(Globals.ALIAS, player);
        intent.putExtra(Globals.TIME_CONTROL, time_control);
        intent.putExtra(Globals.TIME_LEFT, time_left);
        intent.putExtra(Globals.SIZE, grid_size);
        intent.putExtra(Globals.CELLS, cells_left);
        intent.putExtra(Globals.RESULT,result);
        intent.putExtra(Globals.DETAIL_RESULT,detail_result);
        startActivity(intent);
        getActivity().finish();
    }


    private void setTurnText() {
        String statusTurn = "";
        if(game.getStatus() == Status.PLAYER1_PLAYS){
            statusTurn = player;
        }else if(game.getStatus() == Status.PLAYER2_PLAYS && !multiplayer_mode){
            statusTurn = Globals.CPU;
        }else if(game.getStatus() == Status.PLAYER2_PLAYS && multiplayer_mode){
            statusTurn = Globals.PLAYER2;
        }
        this.turn.setText(" " + statusTurn);
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return graella.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(mContext);
                if(getCount() == 25 ){
                    imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                    imageView.setPadding(0, 0, 0, 0);
                }
                else if(getCount() == 36 ){
                    imageView.setLayoutParams(new GridView.LayoutParams(170, 170));
                    imageView.setPadding(0, 0, 0, 0);
                }
                else {
                    imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
                    imageView.setPadding(0, 0, 0, 0);
                }
            }
            else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(graella[position]);
            return imageView;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Globals.GAME, (Parcelable) game);
        outState.putString(Globals.ALIAS, player);
        outState.putInt(Globals.SIZE, grid_size);
        outState.putBoolean(Globals.TIME_CONTROL, time_control);
    }
}
