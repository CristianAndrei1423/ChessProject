package Pieces;

import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    public King(Colors color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = this.getPosition();

        // are toate directiile
        int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};
        Position prevPos;

        //merg doar un pas in toate directiile
        for (int dir : dirs)
            if (board.isValidMove(curPos, prevPos = posDir(curPos, dir), this))
                posMoves.add(prevPos);

        return posMoves;
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
