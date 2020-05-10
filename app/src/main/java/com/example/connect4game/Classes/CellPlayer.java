package com.example.connect4game.Classes;

public class CellPlayer {
    private static final char PLAYER1 = '1';
    private static final char PLAYER2 = '2';

    private final char id;

    private CellPlayer(char id) {
        this.id = id;
    }

    public static CellPlayer player1() {
        return new CellPlayer(PLAYER1);
    }

    public static CellPlayer player2() {
        return new CellPlayer(PLAYER2);
    }

    public boolean isEqualTo(CellPlayer other) {
        return other != null && this.id == other.id;
    }

    public boolean isPlayer1() {
        return this.id == '1';
    }

    public boolean isPlayer2() {
        return this.id == '2';
    }
}
