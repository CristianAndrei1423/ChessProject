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

        for(int i =0;i < 4; i++){
            Position p = from;
            p = posDir(p, dirs[i]);
            while(p.onBoard()){
                if (board.isValidMove(from, p, board.getPieceAt(from))) posMoves.add(p);

                if (board.getPieceAt(p) != null) break;
                p = posDir(p, dirs[i]);
            }
        }

        return posMoves;
    }
}
