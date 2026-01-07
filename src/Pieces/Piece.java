package Pieces;
import MoveStrategies.MoveStrategy;
import Utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Piece implements ChessPiece {
    Colors color;
    Position pos;

    protected MoveStrategy moveStrategy;

    public List<Position> getPossibleMoves(Board board){
        return moveStrategy.getPossibleMoves(board, this.pos);
    }

    public Colors getColor(){
        return color;
    }

    public Piece(Colors color, Position pos, MoveStrategy moveStrategy){
        this.color = color;
        this.pos = pos;
        this.moveStrategy = moveStrategy;
    }

    public Position getPosition(){
        return pos;
    }

    public void setPosition(Position position){
        pos = position;
    }

    /// Returns position when taking one step in a direction
    /// dir is 0 from up - right and goes clockwise to 7 which is up
    public static Position posDir(Position pos, int dir){

        int[] stepX = {1, 1, 1, 0, -1, -1, -1, 0};
        int[] stepY = {1, 0, -1, -1, -1, 0, 1, 1};

        return new Position((char)((int)pos.x + stepX[dir]), pos.y + stepY[dir]);
    }

    /// Don't use for a knight's move, it results in undefined behaviour
    public static int dirFromPositions(Position from, Position to){
        // Implementare : fac pentru un pas pentru fiecare directie si calculez
        // manhattan distance pentru fiecare pas in parte
        // cea mai mica distanta este in directia in care trebuie sa mergi
        int[] dists = new int[8];
        int ans = 999999;

        for(int i = 0 ; i < 8; i++){
            Position newPos = posDir(from, i);
            dists[i] = newPos.manhattanDistance(newPos, to);
            ans = Math.min(ans, dists[i]);
        }

        for(int i = 0;i < 8 ;i++){
            if(dists[i] == ans)
                return i;
        }
        return -1;
    }

    /// Draws axes in the directions given as params and returns a list of
    /// the pieces that intersects them
    public static List<Piece> axesInters(Board board, Position piecePos, int[] dirs){
        // aici vreau sa desenez axe si returnez ce piesa se intersecteaza cu ce axa
        // folosesc aceeasi codificare incepand cu 0 din dreapta sus
        // fac raytracing in sah efectiv
        Piece piece;
        List <Piece> pieces = new ArrayList<Piece>();
        // int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};
        Position prevPos;
        for(int dir : dirs){
            prevPos = piecePos;
            // aici ma uit doar pana unde se poate duce axa respectiva

            while((piece = board.getPieceAt(prevPos = posDir(prevPos, dir))) == null &&
            prevPos.onBoard());

            if(piece != null){
                pieces.add(piece);
            } else pieces.add(null);
        }
        return pieces;
    }

    public boolean equals(Piece p){
        return this.type() == p.type() && p.pos.equals(this.pos);
    }

    @Override
    public String toString(){
        StringBuilder pieceType = new StringBuilder();

        if(this.getColor() == Colors.BLACK)
            pieceType.append("B");
        else pieceType.append("W");

        //pieceType.append(" ");

        switch (this) {
            case Pawn pawn -> pieceType.append("-P");
            case Rook rook -> pieceType.append("-R");
            case Bishop bishop -> pieceType.append("-B");
            case Knight knight -> pieceType.append("-N");
            case Queen queen -> pieceType.append("-Q");
            case King king -> pieceType.append("-K");
            default -> {
            }
        }

        //pieceType.append(" ").append(this.getPosition().toString());

        return pieceType.toString();
    }

    public static void main(String[] args){
        Board testBoard = new Board();
        testBoard.initialize();
        // probabil constructorul , getters si setters nu trb sa testez

        // Testare posDir
        Position posTest = new Position('A', 3);
        posTest = posDir(posTest, 0);
        System.out.println(posTest.toString());

        // Testare dirFromPositions
        Position pos1 = new Position('A', 4);
        Position pos2 = new Position('B', 5);
        System.out.println(dirFromPositions(pos1, pos2));

        // Testare axesInters
        int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};
        Position pos3 = new Position('D', 3);
        System.out.println(axesInters(testBoard, pos3, dirs).toString());

        // Testarea lui toString se face cand se joaca

    }

}
