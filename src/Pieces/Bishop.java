package Pieces;

import MoveStrategies.BishopMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.List;

public class Bishop extends Piece{
    public Bishop(Colors color, Position pos) {
        super(color, pos, new BishopMoveStrategy());
    }
    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        // lone-standing implementation
        int[] dirs = {0, 2, 4, 6};

        List<Piece> pieceList = axesInters(board, kingPosition, dirs);

        return pieceList.contains(this);
    }

    @Override
    public char type() {
        return 'B';
    }
}
