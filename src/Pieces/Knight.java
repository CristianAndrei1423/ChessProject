package Pieces;

import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Colors color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = this.getPosition();

        // aici e putin mai complicat trebuie sa vad pozitiile urmatoare:
        int[] dirX = {1, 2, 1, 2, -1, -2, -1, -2};
        int[] dirY = {2, 1, -2, -1, -2, -1, 2, 1};

        Position nextPos;
        for(int i = 0; i < 8; i++){
            nextPos = new Position((char)((int)curPos.x + dirX[i]), curPos.y + dirY[i]);
            if(board.isValidMove(curPos, nextPos, this)){
                posMoves.add(nextPos);
            }
        }

        return posMoves;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        int[] dirX = {1, 2, 1, 2, -1, -2, -1, -2};
        int[] dirY = {2, 1, -2, -1, -2, -1, 2, 1};

        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = this.getPosition();

        // codul presupune ca mutarea este deja facuta si trebuie doar sa
        // vada ce i se cere, nimic mai mult
        Position nextPos;
        for(int i = 0; i < 8; i++){
            nextPos = new Position((char)((int)curPos.x + dirX[i]), curPos.y + dirY[i]);
            if(nextPos.equals(kingPosition))
                return true;
        }
        return false;
    }

    @Override
    public char type() {
        return 'N';
    }
}
