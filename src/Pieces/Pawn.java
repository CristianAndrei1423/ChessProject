package Pieces;

import MoveStrategies.PawnMoveStrategy;
import Utils.Board;
import Utils.Colors;
import Utils.Position;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Colors color, Position pos) {
        super(color, pos, new PawnMoveStrategy());
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        int[] dirs;

        // change the diagonals based on the color of the piece
        if(this.getColor() == Colors.BLACK)
            dirs = new int[]{0, 6};
        else dirs = new int[]{2, 4};

        List<Piece> pieceList = axesInters(board, kingPosition, dirs);

        // if the found piece is a pawn and the distance between the pawn and the king is 1 then it's checked
        return pieceList.contains(this) && Position.trajectory(this.getPosition(), kingPosition).size() == 1;
    }

    @Override
    public char type() {
        return 'P';
    }
}
