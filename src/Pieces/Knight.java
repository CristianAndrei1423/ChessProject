package Pieces;

import MoveStrategies.KnightMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

public class Knight extends Piece {
    public Knight(Colors color, Position pos) {
        super(color, pos, new KnightMoveStrategy());
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        int[] dirX = {1, 2, 1, 2, -1, -2, -1, -2};
        int[] dirY = {2, 1, -2, -1, -2, -1, 2, 1};

        Position curPos = this.getPosition();

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
