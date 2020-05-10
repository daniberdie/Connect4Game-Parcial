package com.example.connect4game.Classes;

import android.os.CountDownTimer;

import com.example.connect4game.Activities.GameActivity;

public class TimeCounter extends CountDownTimer {

    private long timeLeft;
    private Game game;
    private GameActivity gameActivity;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCounter(long millisInFuture, long countDownInterval, Game game, GameActivity gameActivity) {
        super(millisInFuture, countDownInterval);
        this.game = game;
        this.gameActivity = gameActivity;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        this.timeLeft = millisUntilFinished;
    }

    @Override
    public void onFinish() {
        game.setStatus(Status.DRAW);
        gameActivity.time_left = 0;
        gameActivity.checkFinish();
    }

    public long getTime(){
        return timeLeft;
    }

    public void finalizeCounter(){
        this.cancel();
    }
}
