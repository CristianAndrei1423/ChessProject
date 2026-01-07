package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

import static Pieces.Piece.posDir;

public class KingMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = from;

        // are toate directiile
        int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};
        Position prevPos;

        //merg doar un pas in toate directiile
        for (int dir : dirs)
            if (board.isValidMove(curPos, prevPos = posDir(curPos, dir), board.getPieceAt(from)))
                posMoves.add(prevPos);

        return posMoves;
    }
}
