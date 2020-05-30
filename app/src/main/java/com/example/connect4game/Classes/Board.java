package com.example.connect4game.Classes;


public class Board{
    private final int size;
    private final CellPlayer[][] cells;

    public Board(int size) {
        this.size = size;
        this.cells = new CellPlayer[this.size][this.size];

        for (int i = 0; i < this.size; ++i) {
            for (int j = 0; j < this.size; ++j) {
                cells[i][j] = null;
            }
        }
    }

    public boolean canPlayColumn(int column) {
        boolean canPlay = false;
        if(column > 0 || column <= this.size){
            if(lastEmptyRow(column) >= 0){
                canPlay = true;
            }
        }
        return canPlay;
    }

    public boolean hasValidMoves() {
        for (int j = 0; j < this.size; j++) {
            if (lastEmptyRow(j) >= 0) {
                return true;
            }
        }
        return false;
    }

    public Position occupyCell(int column, CellPlayer player) {
        if (canPlayColumn(column)) {
            int newRow = lastEmptyRow(column);
            Position pos = new Position(newRow, column); // Crear la nova posició
            cells[newRow][column] = player; // Afegir el id del jugador a la matriu
            return pos;
        } else {
            return null;
        }
    }

    public int lastEmptyRow(int column) {
        int lastRow = -1;
        if (column > 0 || column <= this.size) {
            for (int i = 0; i < this.size; ++i) {
                if (cells[i][column] == null) {
                    lastRow++;
                }
            }
        }
        return lastRow;
    }

    public int maxConnected(Position position) {

        int max = 0;

        int maxHorizontal = checkHoritzontal(position);
        int maxVertical = checkVertical(position);
        int maxMainDiagonal = checkMainDiagonal(position);
        int maxContraDiagonal = checkContraDiagonal(position);

        if(maxHorizontal > max){
            max = maxHorizontal;
        }
        if (maxVertical > max){
            max = maxVertical;
        }
        if (maxMainDiagonal > max) {
            max = maxMainDiagonal;
        }
        if (maxContraDiagonal > max){
            max = maxContraDiagonal;
        }

        return max;
    }

    private int checkContraDiagonal(Position position) {
        return getPiecesConnectedInDirection(position, Direction.CONTRA_DIAGONAL) + getPiecesConnectedInDirection(position, Direction.CONTRA_DIAGONAL.invert()) - 1;
    }

    private int checkMainDiagonal(Position position) {
        return getPiecesConnectedInDirection(position, Direction.MAIN_DIAGONAL) + getPiecesConnectedInDirection(position, Direction.MAIN_DIAGONAL.invert()) - 1;
    }

    private int checkVertical(Position position) {
        return getPiecesConnectedInDirection(position, Direction.DOWN) + getPiecesConnectedInDirection(position, Direction.DOWN.invert()) - 1;
    }

    private int checkHoritzontal(Position position) {
        return getPiecesConnectedInDirection(position, Direction.RIGHT) + getPiecesConnectedInDirection(position, Direction.RIGHT.invert()) - 1;
    }

    private int getPiecesConnectedInDirection(Position position, Direction direction) {
        int pieces = 1;

        Position posMoved = position.move(direction);
        if(!position.checkOutOfRange(this.size) && !posMoved.checkOutOfRange(this.size)){
            while (posMoved.getColumn() < this.size && cells[position.getRow()][position.getColumn()].isEqualTo(cells[posMoved.getRow()][posMoved.getColumn()])) {
                pieces++;
                posMoved = posMoved.move(direction);
                if(posMoved.checkOutOfRange(this.size)) break;
            }
        }

        return pieces;
    }
}
