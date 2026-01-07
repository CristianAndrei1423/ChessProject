package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = from;

        // aici e putin mai complicat trebuie sa vad pozitiile urmatoare:
        int[] dirX = {1, 2, 1, 2, -1, -2, -1, -2};
        int[] dirY = {2, 1, -2, -1, -2, -1, 2, 1};

        Position nextPos;
        for(int i = 0; i < 8; i++){
            nextPos = new Position((char)((int)curPos.x + dirX[i]), curPos.y + dirY[i]);
            if(board.isValidMove(curPos, nextPos, board.getPieceAt(from))){
                posMoves.add(nextPos);
            }
        }

        return posMoves;
    }
}
