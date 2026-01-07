package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

import static Pieces.Piece.posDir;

public class RookMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = from;

        // are doar directiile 1, 3, 5, 7 -> pe linie dreapta
        int[] dirs = {1, 3, 5, 7};
        Position prevPos;

        for(int dir : dirs){
            Position p = curPos;
            while(true){
                p = posDir(p, dir);
                if (!p.onBoard()) break;

                if (board.isValidMove(curPos, p, board.getPieceAt(from)))
                    posMoves.add(p);

                if (board.getPieceAt(p) != null)
                    break;
            }
        }

        return posMoves;
    }
}
