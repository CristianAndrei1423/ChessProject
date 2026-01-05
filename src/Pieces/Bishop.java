package Pieces;

import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    public Bishop(Colors color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        // se poate duce in 4 directii pe diagonala
        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = this.getPosition();

        // are doar directiile 0, 2, 4, 6
        int[] dirs = {0, 2, 4, 6};
        Position prevPos;

        for(int dir : dirs){
            // pentru fiecare directie creez o pozitie in aceea directie
            prevPos = curPos;
            // crazy ce poti face cu java-u asta VVV
            while(board.isValidMove(prevPos, prevPos = posDir(prevPos, dir), this)){
                posMoves.add(prevPos);
            }
        }

        return posMoves;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        // implementarea cu getPossibleMoves rezulta intr-o
        // dependenta circulara
        // voi implementa prin a trasa linii aferente pentru
        // fiecare piesa in parte
        int[] dirs = {0, 2, 4, 6}; // in diagonale

        List<Piece> pieceList = axesInters(board, kingPosition, dirs);

        return pieceList.contains(this);
    }

    @Override
    public char type() {
        return 'B';
    }
}
