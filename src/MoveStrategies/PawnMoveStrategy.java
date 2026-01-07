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

        if(curPiece.getColor() == Colors.BLACK){
            // need to see if something in front because it can't be captured
            if(board.isValidMove(from, nextPos = posDir(from, 3), curPiece) &&
                    board.getPieceAt(nextPos) == null){
                posMoves.add(nextPos);
                // only if it can move 1 step in front, can it move 2 steps in front
                if(board.isValidMove(from, nextPos = posDir(nextPos, 3), curPiece) &&
                        board.getPieceAt(nextPos) == null && from.y == 7){
                    posMoves.add(nextPos);
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
            if(board.isValidMove(from, nextPos = posDir(from, 7), curPiece) &&
                    board.getPieceAt(nextPos) == null){
                posMoves.add(nextPos);
                // only if it can move 1 step in front, can it move 2 steps in front
                if(board.isValidMove(from, nextPos = posDir(nextPos, 7), curPiece) &&
                        board.getPieceAt(nextPos) == null && from.y == 2){
                    posMoves.add(nextPos);
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
