package Utils;

import Exceptions.InvalidMoveException;
import Pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Player {
    String name;
    public Colors pieceColor;
    private List<Piece> capturedPieces;
    public int nrofCapturedPieces;
    private TreeSet<ChessPair<Position, Piece>> ownedPieces;
    private int points;

    public Player(String email, Colors color){
        name = email;
        pieceColor = color;
        ownedPieces = new TreeSet<ChessPair<Position, Piece>>();
        capturedPieces = new ArrayList<Piece>();
        points = 0;
        nrofCapturedPieces = 0;
    }

    public void makeMove(Position from, Position to, Board board, Game game, Player opp) {
        Move move = board.movePiece(from, to, this, opp);
        game.moveList.add(move);

    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public List<ChessPair<Position, Piece>> getOwnedPieces(){
        return ownedPieces.stream().toList();
    }

    public void addOwnedPiece(ChessPair<Position, Piece> ps){
        ownedPieces.add(ps);
    }

    public void removeOwnedPiece(ChessPair<Position, Piece> piece){
        ownedPieces.remove(piece);
    }

    public void addCapturedPiece(Piece piece){
        capturedPieces.add(piece);
    }

    public int getPoints(){
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString(){
        return "Player : " + name + " as color " + pieceColor.toString();
    }

}
