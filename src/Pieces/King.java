package Pieces;

import MoveStrategies.KingMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    public King(Colors color, Position pos) {
        super(color, pos, new KingMoveStrategy());
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        // nu poti da check cu regele altui rege
        return false;
    }

    @Override
    public char type() {
        return 'K';
    }
    // yes king
}
