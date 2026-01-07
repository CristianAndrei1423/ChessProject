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

        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = from;
        Position nextPos;
        Piece curPiece = board.getPieceAt(from);

        // Trebuie sa vad ce culoare este
        if(curPiece.getColor() == Colors.BLACK){
            // aici trebuie sa ma uit si daca e ceva in fata lui unde vrea sa mearga
            // ca nu poate sa captureze piese din fata lui naiba
            if(board.isValidMove(curPos, nextPos = posDir(curPos, 3), curPiece) &&
                    board.getPieceAt(nextPos) == null){
                posMoves.add(nextPos);
                // doar daca se poate misca cu unu in fata se poate misca cu 2 in fata
                if(board.isValidMove(curPos, nextPos = posDir(nextPos, 3), curPiece) &&
                        board.getPieceAt(nextPos) == null && curPos.y == 7){
                    posMoves.add(nextPos);
                }
            }
            // vad daca in diagonale sunt piese, si daca le pot captura
            Piece ps;
            if((ps = board.getPieceAt(nextPos = posDir(curPos, 2))) != null &&
                    board.isValidMove(curPos, nextPos, curPiece) && ps.getColor() != curPiece.getColor())
                posMoves.add(nextPos);

            if((ps = board.getPieceAt(nextPos = posDir(curPos, 4))) != null &&
                    board.isValidMove(curPos, nextPos, curPiece) && ps.getColor() != curPiece.getColor())
                posMoves.add(nextPos);


        } else {
            if(board.isValidMove(curPos, nextPos = posDir(curPos, 7), curPiece) &&
                    board.getPieceAt(nextPos) == null){
                posMoves.add(nextPos);
                // doar daca se poate misca cu unu in fata se poate misca cu 2 in fata
                if(board.isValidMove(curPos, nextPos = posDir(nextPos, 7), curPiece) &&
                        board.getPieceAt(nextPos) == null && curPos.y == 2){
                    posMoves.add(nextPos);
                }
            }

            // vad diagonalele
            Piece ps;
            if((ps = board.getPieceAt(nextPos = posDir(curPos, 0))) != null &&
                    board.isValidMove(curPos, nextPos, curPiece) && ps.getColor() != curPiece.getColor())
                posMoves.add(nextPos);

            if((ps = board.getPieceAt(nextPos = posDir(curPos, 6))) != null &&
                    board.isValidMove(curPos, nextPos, curPiece) && ps.getColor() != curPiece.getColor())
                posMoves.add(nextPos);

        }

        return posMoves;
    }
}
