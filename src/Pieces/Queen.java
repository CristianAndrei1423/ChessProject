package Pieces;

import MoveStrategies.QueenMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    public Queen(Colors color, Position pos) {
        super(color, pos, new QueenMoveStrategy());
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
