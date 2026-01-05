package Utils;

import Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Position {
    public char x;
    public int y;

    public Position(char x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(){
        this.x = 0;
        this.y = 0;
    }

    public int compare(Object o1, Object o2) {
        // 2 obiecte de tip Position se compara intre ele crescator dupa coord y si,
        // daca aceasta este e egala, crescator dupa coord x

        if(o1 instanceof Position a && o2 instanceof Position b) {
            if(a.y > b.y){
                return 1;
            } else if (a.y < b.y){
                return -1;
            } else {
                if(a.x > b.x) {
                    return 1;
                } else if (a.x < b.x) {
                    return -1;
                }
            }
        }
        return 0;
    }

    public boolean equals(Object o) {
        if(o instanceof Position){
            return this.y == ((Position) o).y && this.x == ((Position) o).x;
        }
        return false;
    }

    public boolean onBoard(){
        return (this.y >= 1 && this.y <= 8 && this.x >= 'A' && this.x <= 'H');
    }

    @Override
    public String toString(){
        return "" + x + y;
    }

    /// Returns a position from a string, returns null for invalid string
    public static Position fromString(String posString){
        char x_new = posString.charAt(0);
        int y_new = posString.charAt(1) - '0';

        if(x_new >= 'A' && x_new <= 'H' && y_new >= 1 && y_new <= 8)
            return new Position(x_new, y_new);

        return null;
    }

    /// Returns a list of positions between 2 positions starting from position from
    public static List<Position> trajectory(Position from, Position to){
        int dir = Piece.dirFromPositions(from, to);
        List<Position> ans = new ArrayList<Position>();

        Position cur = new Position(from.x, from.y);

        while(!cur.equals(to)){
            ans.add(cur);
            cur = Piece.posDir(cur, dir);
        }

        return ans;
    }

    public int manhattanDistance(Position a, Position b){
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public static void main(String[] args){
        // testare fromString
        Position pos = fromString("A3");
        System.out.println(pos.toString());

        // testare trajectory
        Position pos1 = new Position('A', 3);
        Position pos2 = new Position('F', 8);
        System.out.println(trajectory(pos1, pos2).toString());
    }

}
