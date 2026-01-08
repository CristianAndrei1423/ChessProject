package Utils;

import Pieces.Piece;

public class Move {
    private Colors playerColor;
    private Position from, to;
    private Piece piece;

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

    @Override
    public String toString(){
        return from.toString() + " -> " + to.toString();
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Move) {
            Move move = (Move) obj;
            return this.from.equals(move.from) && this.to.equals(move.to);
        }
        return false;
    }

}
