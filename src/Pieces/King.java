package Pieces;

import MoveStrategies.KingMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

public class King extends Piece{
    public King(Colors color, Position pos) {
        super(color, pos, new KingMoveStrategy());
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        // you can't check a king with another king
        return false;
    }

    @Override
    public char type() {
        return 'K';
    }
    // yes king
}
