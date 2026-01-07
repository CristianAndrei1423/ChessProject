package Pieces;

import MoveStrategies.BishopMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{
    public Bishop(Colors color, Position pos) {
        super(color, pos, new BishopMoveStrategy());
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
