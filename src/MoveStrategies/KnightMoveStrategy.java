package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> posMoves = new ArrayList<>();

        int[] dirX = {1, 2, 1, 2, -1, -2, -1, -2};
        int[] dirY = {2, 1, -2, -1, -2, -1, 2, 1};

        Position nextPos;
        for(int i = 0; i < 8; i++){
            nextPos = new Position((char)((int)from.x + dirX[i]), from.y + dirY[i]);
            if(board.isValidMove(from, nextPos, board.getPieceAt(from))){
                posMoves.add(nextPos);
            }
        }

        return posMoves;
    }
}
