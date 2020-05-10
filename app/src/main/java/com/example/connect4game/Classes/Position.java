package com.example.connect4game.Classes;

public class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public Position move(Direction direction) {
        return new Position(this.row + direction.getChangeInRow(), this.column + direction.getChangeInColumn());
    }

    public boolean checkOutOfRange(int size){
        return (this.getColumn() < 0 || this.getRow() < 0 || this.getColumn() >= size || this.getRow() >= size);
    }
}
