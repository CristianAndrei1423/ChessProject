package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

import static Pieces.Piece.posDir;

public class BishopMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        // it can go in all 4 diagonals
        List<Position> posMoves = new ArrayList<Position>();

        int[] dirs = {0, 2, 4, 6};

        for(int i = 0; i< 4;i ++){
            Position p = from;
            p = posDir(p, dirs[i]);
            while(p.onBoard()){
                if (board.isValidMove(from, p, board.getPieceAt(from))) posMoves.add(p);

                // check if there is something there
                if (board.getPieceAt(p) != null) break;

                p = posDir(p, dirs[i]);
            }
        }

        return posMoves;
    }
}
