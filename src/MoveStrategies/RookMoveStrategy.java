package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

import static Pieces.Piece.posDir;

public class RookMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> posMoves = new ArrayList<>();

        int[] dirs = {1, 3, 5, 7};

        for(int dir : dirs){
            Position p = from;
            while(true){
                p = posDir(p, dir);
                if (!p.onBoard()) break;

                if (board.isValidMove(from, p, board.getPieceAt(from)))
                    posMoves.add(p);

                if (board.getPieceAt(p) != null)
                    break;
            }
        }

        return posMoves;
    }
}
