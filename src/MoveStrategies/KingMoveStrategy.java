package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

import static Pieces.Piece.posDir;

public class KingMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> posMoves = new ArrayList<>();

        // has all directions
        int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};
        Position prevPos;

        // only one step in each direction
        for (int i = 0;i < 8;i ++)
            if (board.isValidMove(from, prevPos = posDir(from, dirs[i]), board.getPieceAt(from)))
                posMoves.add(prevPos);

        return posMoves;
    }
}
