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

    public boolean isEqualTo(Position other) {
        return other != null && this.row == other.row && this.column == other.column;
    }

    public boolean checkOutOfRange(int size){
        return (this.getColumn() < 0 || this.getRow() < 0 || this.getColumn() >= size || this.getRow() >= size);
    }

    private boolean isMarginLeft(){
        boolean ret = false;

        if(this.getColumn() <= 0){
            ret = true;
        }
        return ret;
    }

    private boolean isMarginRight(int size){
        boolean ret = false;

        if(this.getColumn() >= size-1){
            ret = true;
        }
        return ret;
    }

    private boolean isMarginDown(int size){
        boolean ret = false;

        if(this.getRow() >= size-1){
            ret = true;
        }
        return ret;
    }

    private boolean isMarginUp(){
        boolean ret = false;

        if(this.getRow() <= 0){
            ret = true;
        }
        return ret;
    }

    private boolean isCornerLeftTop(){
        boolean ret = false;

        if(this.getRow() == 0 && this.getColumn() == 0){
            ret = true;
        }
        return ret;
    }

    private boolean isCornerLeftDown(int size){
        boolean ret = false;

        if(this.getRow() == size-1 && this.getColumn() == 0){
            ret = true;
        }
        return ret;
    }

    private boolean isCornerRightTop(int size){
        boolean ret = false;

        if(this.getRow() == 0 && this.getColumn() == size-1){
            ret = true;
        }
        return ret;
    }

    private boolean isCornerRightDown(int size){
        boolean ret = false;

        if(this.getRow() == size-1 && this.getColumn() == size-1){
            ret = true;
        }
        return ret;
    }

    public static int pathLength(Position pos1, Position pos2) {
        if(pos1.isEqualTo(pos2)){
            return 1;
        }else{
            int fila, columna;

            fila = pos2.getRow() - pos1.getRow();
            columna = pos2.getColumn() - pos1.getColumn();

            fila = Math.abs(fila) + 1;
            columna = Math.abs(columna) + 1;

            if (fila >= columna) {
                return fila;
            } else {
                return columna;
            }
        }
    }
}
