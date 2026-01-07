package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

import static Pieces.Piece.posDir;

public class BishopMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        // se poate duce in 4 directii pe diagonala
        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = from;

        // are doar directiile 0, 2, 4, 6
        int[] dirs = {0, 2, 4, 6};
        Position prevPos;

        for(int dir : dirs){
            Position p = curPos;
            while(true){
                p = posDir(p, dir);

                if (!p.onBoard()) break;

                if (board.isValidMove(curPos, p, board.getPieceAt(from))) {
                    posMoves.add(p);
                }

                if (board.getPieceAt(p) != null) {
                    break;
                }
            }
        }

        return posMoves;
    }
}
