package Utils;

import Pieces.Piece;

public class Move {
    private Colors playerColor;
    private Position from, to;
    private Piece capturedPiece;

    public Move(Colors pColor, Position from, Position to){
        playerColor = pColor;
        this.from = from;
        this.to = to;
    }

    public Position getFrom(){
        return from;
    }

    public Position getTo(){
        return to;
    }

    public Colors getPlayerColor(){return playerColor;}

}
