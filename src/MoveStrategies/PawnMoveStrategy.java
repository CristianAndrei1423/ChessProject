package MoveStrategies;

import Pieces.Piece;
import Utils.Board;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

import static Pieces.Piece.posDir;

public class PawnMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {

        List<Position> posMoves = new ArrayList<>();
        Position nextPos;
        Piece curPiece = board.getPieceAt(from);

        if (curPiece == null) return posMoves;

        if(curPiece.getColor() == Colors.BLACK){
            // need to see if something in front because it can't be captured
            Position forward1 = posDir(from, 3);
            if(board.isValidMove(from, forward1, curPiece) &&
                    board.getPieceAt(forward1) == null){
                posMoves.add(forward1);

                // only if it doesn't have something in front of it can it move 2 time in front
                if (from.y == 7) {
                    Position forward2 = posDir(forward1, 3);
                    if(board.getPieceAt(forward2) == null &&
                            board.isValidMove(from, forward2, curPiece)){
                        posMoves.add(forward2);
                    }
                }
            }

            // see if there are pieces on the diagonal that can be captured
            Piece ps;
            if((ps = board.getPieceAt(nextPos = posDir(from, 2))) != null &&
                    board.isValidMove(from, nextPos, curPiece) && ps.getColor() != curPiece.getColor())
                posMoves.add(nextPos);

            if((ps = board.getPieceAt(nextPos = posDir(from, 4))) != null &&
                    board.isValidMove(from, nextPos, curPiece) && ps.getColor() != curPiece.getColor())
                posMoves.add(nextPos);


        } else {
            Position forward1 = posDir(from, 7);
            if(board.isValidMove(from, forward1, curPiece) &&
                    board.getPieceAt(forward1) == null){
                posMoves.add(forward1);

                // only if it doesn't have something in front of it can it move 2 time in front
                if (from.y == 2) {
                    Position forward2 = posDir(forward1, 7);
                    if(board.getPieceAt(forward2) == null &&
                            board.isValidMove(from, forward2, curPiece)){
                        posMoves.add(forward2);
                    }
                }
            }

            // see diagonals
            Piece ps;
            if((ps = board.getPieceAt(nextPos = posDir(from, 0))) != null &&
                    board.isValidMove(from, nextPos, curPiece) && ps.getColor() != curPiece.getColor())
                posMoves.add(nextPos);

            if((ps = board.getPieceAt(nextPos = posDir(from, 6))) != null &&
                    board.isValidMove(from, nextPos, curPiece) && ps.getColor() != curPiece.getColor())
                posMoves.add(nextPos);

        }

        return posMoves;
    }
}
