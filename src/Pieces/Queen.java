package Pieces;

import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    public Queen(Colors color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = this.getPosition();

        // are toate directiile
        int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};
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
        int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};

        List<Piece> pieceList = axesInters(board, kingPosition, dirs);

        return pieceList.contains(this);
    }

    @Override
    public char type() {
        return 'Q';
    }
    ///  yas queen
}
