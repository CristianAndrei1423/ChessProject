package Pieces;

import Utils.Board;
import Utils.Colors;
import Utils.Position;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Colors color, Position pos) {
        super(color, pos);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {

        List<Position> posMoves = new ArrayList<Position>();
        Position curPos = this.getPosition();
        Position nextPos;

        // Trebuie sa vad ce culoare este
        if(this.getColor() == Colors.BLACK){
            // aici trebuie sa ma uit si daca e ceva in fata lui unde vrea sa mearga
            // ca nu poate sa captureze piese din fata lui naiba
            if(board.isValidMove(curPos, nextPos = posDir(curPos, 3), this) &&
                    board.getPieceAt(nextPos) == null){
                posMoves.add(nextPos);
                // doar daca se poate misca cu unu in fata se poate misca cu 2 in fata
                if(board.isValidMove(curPos, nextPos = posDir(nextPos, 3), this) &&
                        board.getPieceAt(nextPos) == null && curPos.y == 7){
                    posMoves.add(nextPos);
                }
            }
            // vad daca in diagonale sunt piese, si daca le pot captura
            Piece ps;
            if((ps = board.getPieceAt(nextPos = posDir(curPos, 2))) != null &&
                    board.isValidMove(curPos, nextPos, this) && ps.getColor() != this.getColor())
                posMoves.add(nextPos);

            if((ps = board.getPieceAt(nextPos = posDir(curPos, 4))) != null &&
                    board.isValidMove(curPos, nextPos, this) && ps.getColor() != this.getColor())
                posMoves.add(nextPos);


        } else {
            if(board.isValidMove(curPos, nextPos = posDir(curPos, 7), this) &&
                    board.getPieceAt(nextPos) == null){
                posMoves.add(nextPos);
                // doar daca se poate misca cu unu in fata se poate misca cu 2 in fata
                if(board.isValidMove(curPos, nextPos = posDir(nextPos, 7), this) &&
                    board.getPieceAt(nextPos) == null && curPos.y == 2){
                    posMoves.add(nextPos);
                }
            }

            // vad diagonalele
            Piece ps;
            if((ps = board.getPieceAt(nextPos = posDir(curPos, 0))) != null &&
                    board.isValidMove(curPos, nextPos, this) && ps.getColor() != this.getColor())
                posMoves.add(nextPos);

            if((ps = board.getPieceAt(nextPos = posDir(curPos, 6))) != null &&
                    board.isValidMove(curPos, nextPos, this) && ps.getColor() != this.getColor())
                posMoves.add(nextPos);

        }

        return posMoves;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        int[] dirs; // in diagonale

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
