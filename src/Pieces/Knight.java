package Pieces;

import MoveStrategies.KnightMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Colors color, Position pos) {
        super(color, pos, new KnightMoveStrategy());
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
