package Pieces;

import MoveStrategies.RookMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
    public Rook(Colors color, Position pos) {
        super(color, pos, new RookMoveStrategy());
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        int[] dirs = {1, 3, 5, 7};

        List<Piece> pieceList = axesInters(board, kingPosition, dirs);

        return pieceList.contains(this);
    }

    @Override
    public char type() {
        return 'R';
    }
}
