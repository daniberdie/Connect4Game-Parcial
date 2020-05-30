package com.example.connect4game.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Game {
    private final Board board;
    private final int toWin;
    private final int size;

    private Status status;

    public Game(int size, int toWin) {

        this.toWin = toWin;
        this.board = new Board(size);
        this.status = Status.PLAYER1_PLAYS;
        this.size = size;
    }

    protected Game(Parcel in) {
        toWin = in.readInt();
        size = in.readInt();
        board = (Board) in.readSerializable();
        status = (Status) in.readSerializable();
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status){this.status = status;}

    public boolean canPlayColumn(int column) {
        return board.canPlayColumn(column);
    }

    public int getPositionOfLastEmptyRowInColumn(int column){
        return board.lastEmptyRow(column) * size + column;
    }

    private void toggleTurn(Status newStatus) {
        if (!board.hasValidMoves()) {
            this.status = Status.DRAW;
        } else {
            this.status = newStatus;
        }
    }

    private void firstplayer(Position pos) {
        if (board.maxConnected(pos) >= toWin) {
            this.status = Status.PLAYER1_WINS;
        } else {
            toggleTurn(Status.PLAYER2_PLAYS);
        }
    }

    private void secondplayer(Position pos) {
        if (board.maxConnected(pos) >= toWin) {
            this.status = Status.PLAYER2_WINS;
        } else {
            toggleTurn(Status.PLAYER1_PLAYS);
        }
    }

    public Position drop(int column) {
        if (status == status.PLAYER1_PLAYS) {
            Position pos = board.occupyCell(column, CellPlayer.player1());
            firstplayer(pos);
            return pos;
        } else {
            Position pos = board.occupyCell(column, CellPlayer.player2());
            secondplayer(pos);
            return pos;
        }
    }

    public boolean isFinished() {
        return this.status == Status.PLAYER1_WINS || this.status == Status.PLAYER2_WINS || this.status == Status.DRAW;
    }
}
