package Utils;

import Pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class Board {

    TreeSet<ChessPair<Position, Piece>> pieces;

    public Board(){
        this.pieces = new TreeSet<ChessPair<Position, Piece>>();
    }

    public Board(TreeSet<ChessPair<Position, Piece>> pieces){
        this.pieces = pieces;
    }

    public void initialize(){
        // initializeaza tabla de sah cu pozitiile initiale creand obiectele de tip Piece si adaugandu-le
        // in lista interna

        // se asigura ca pozitia stocata in Piece si pozitia stocata din ChessPair sunt consistente
        Piece rookB1 = new Rook(Colors.BLACK, new Position('A',8));
        pieces.add(new ChessPair<>(rookB1.getPosition(), rookB1));
        Piece knightB1 = new Knight(Colors.BLACK, new Position('B', 8));
        pieces.add(new ChessPair<>(knightB1.getPosition(), knightB1));
        Piece bishopB1 = new Bishop(Colors.BLACK, new Position('C',8));
        pieces.add(new ChessPair<>(bishopB1.getPosition(), bishopB1));
        Piece queenB = new Queen(Colors.BLACK, new Position('D',8));
        pieces.add(new ChessPair<>(queenB.getPosition(), queenB));
        Piece kingB = new King(Colors.BLACK, new Position('E', 8));
        pieces.add(new ChessPair<>(kingB.getPosition(), kingB));
        Piece bishopB2 = new Bishop(Colors.BLACK, new Position('F', 8));
        pieces.add(new ChessPair<>(bishopB2.getPosition(), bishopB2));
        Piece knightB2 = new Knight(Colors.BLACK, new Position('G', 8));
        pieces.add(new ChessPair<>(knightB2.getPosition(), knightB2));
        Piece rookB2 = new Rook(Colors.BLACK, new Position('H', 8));
        pieces.add(new ChessPair<>(rookB2.getPosition(), rookB2));
        Piece[] pawnsB = new Piece[8];
        // ii initializez de la stanga la dreapta
        for(int i = 0;i < 8 ;i++){
            char aux = (char)((int)'A' + i);
            pawnsB[i] = new Pawn(Colors.BLACK, new Position(aux, 7));
            pieces.add(new ChessPair<>(pawnsB[i].getPosition(), pawnsB[i]));
        }

        Piece rookW1 = new Rook(Colors.WHITE, new Position('A',1));
        pieces.add(new ChessPair<>(rookW1.getPosition(), rookW1));
        Piece knightW1 = new Knight(Colors.WHITE, new Position('B', 1));
        pieces.add(new ChessPair<>(knightW1.getPosition(), knightW1));
        Piece bishopW1 = new Bishop(Colors.WHITE, new Position('C',1));
        pieces.add(new ChessPair<>(bishopW1.getPosition(), bishopW1));
        Piece queenW = new Queen(Colors.WHITE, new Position('D',1));
        pieces.add(new ChessPair<>(queenW.getPosition(), queenW));
        Piece kingW = new King(Colors.WHITE, new Position('E', 1));
        pieces.add(new ChessPair<>(kingW.getPosition(), kingW));
        Piece bishopW2 = new Bishop(Colors.WHITE, new Position('F', 1));
        pieces.add(new ChessPair<>(bishopW2.getPosition(), bishopW2));
        Piece knightW2 = new Knight(Colors.WHITE, new Position('G', 1));
        pieces.add(new ChessPair<>(knightW2.getPosition(), knightW2));
        Piece rookW2 = new Rook(Colors.WHITE, new Position('H', 1));
        pieces.add(new ChessPair<>(rookW2.getPosition(), rookW2));
        Piece[] pawnsW = new Piece[8];
        // ii initializez de la stanga la dreapta
        for(int i = 0;i < 8 ;i++){
            char aux = (char)((int)'A' + i);
            pawnsW[i] = new Pawn(Colors.WHITE, new Position(aux, 2));
            pieces.add(new ChessPair<>(pawnsW[i].getPosition(), pawnsW[i]));
        }
    }

    public Move movePiece(Position from, Position to, Player player, Player op){
        Piece piece = getPieceAt(from);

        if(getPieceAt(from) == null)
            return null;

        if(!piece.getPossibleMoves(this).contains(to))
            return null;

        if(isValidMove(from, to, piece)){
            for(ChessPair<Position, Piece> pair : pieces){
                if(pair.getKey().equals(from)){
                    // check if piece is a pawn and got to opposite side
                    if(pair.getValue() instanceof Pawn){
                        if(pair.getValue().getColor().equals(Colors.BLACK) && to.y == 1){
                            // PAWN TRANSFORMS
                            // update on board
                            Piece Queen = new Queen(pair.getValue().getColor(), pair.getValue().getPosition());

                            // update in Player owned pieces
                            for(ChessPair<Position, Piece> pairs : player.getOwnedPieces())
                                if(pairs.equals(pair))
                                    pairs.setValue(Queen);

                            pair.setValue(Queen);
                        }
                    }

                    // check if move results in capture
                    if(getPieceAt(to) != null && getPieceAt(to).getColor() != pair.getValue().getColor()){
                        // check if it is a pawn and if it is, check if move results in capture
                        if(pair.getValue() instanceof Pawn){
                            int dirOfMove = Piece.dirFromPositions(from, to);
                            if(pair.getValue().getColor() == Colors.BLACK){
                                if(dirOfMove == 2 || dirOfMove == 4){
                                    System.out.println("Updated player points");
                                    // update player points
                                    player.setPoints(player.getPoints() + pointsFromCapture(getPieceAt(to)));

                                    // update captured pieces of the player
                                    player.addCapturedPiece(getPieceAt(to));

                                    // update captured piece on board and on pieces of the opponent
                                    ChessPair<Position, Piece> ps = new ChessPair<Position, Piece>(to, getPieceAt(to));

                                    System.out.println("Remove captured piece from board");
                                    // remove captured piece from board
                                    removePiece(ps);

                                    System.out.println("Remove captured piece from opponent owned piece");
                                    op.removeOwnedPiece(ps);
                                }
                            } else {
                                if(dirOfMove == 0 || dirOfMove == 6){
                                    System.out.println("Updated player points");
                                    // update player points
                                    player.setPoints(player.getPoints() + pointsFromCapture(getPieceAt(to)));

                                    // update captured pieces of the player
                                    player.addCapturedPiece(getPieceAt(to));

                                    // update captured piece on board and on pieces of the opponent
                                    ChessPair<Position, Piece> ps = new ChessPair<Position, Piece>(to, getPieceAt(to));

                                    System.out.println("Remove captured piece from board");
                                    // remove captured piece from board
                                    removePiece(ps);

                                    System.out.println("Remove captured piece from opponent owned piece");
                                    op.removeOwnedPiece(ps);
                                }
                            }
                        }
                        else {
                            System.out.println("Updated player points");
                            // update player points
                            player.setPoints(player.getPoints() + pointsFromCapture(getPieceAt(to)));

                            // update captured pieces of the player
                            player.addCapturedPiece(getPieceAt(to));

                            // update captured piece on board and on pieces of the opponent
                            ChessPair<Position, Piece> ps = new ChessPair<Position, Piece>(to, getPieceAt(to));

                            System.out.println("Remove captured piece from board");
                            // remove captured piece from board
                            removePiece(ps);

                            System.out.println("Remove captured piece from opponent owned piece");
                            op.removeOwnedPiece(ps);
                        }
                    }

                    // update the position of the moved piece
                    piece.setPosition(to);

                    System.out.println("Removed old pair " + pair.getKey().toString() + " " + pair.getValue().toString());
                    player.removeOwnedPiece(pair);

                    pieces.remove(pair);
                    pair.setKey(to);
                    pieces.add(pair);

                    System.out.println("Added new pair " + to.toString() + " " + pair.getValue().toString());
                    player.addOwnedPiece(new ChessPair<Position, Piece>(to, pair.getValue()));



                    return new Move(player.pieceColor, from, to);
                }
            }
        }
        return null;
    }

    public Piece getPieceAt(Position position) {
        for(ChessPair<Position, Piece> pair : pieces){
            if(pair.getKey().equals(position)){
                return pair.getValue();
            }
        }
        return null;
    }

    public boolean isValidMove(Position from, Position to, Piece piece){
        // IMPLEMENTARE :
        // - dau check mai intai sa vad daca to e in bounds
        if((to.x >= 'A' && to.x <= 'H') && (to.y >= 1 && to.y <= 8) && (getPieceAt(from) == null ||
                getPieceAt(from).equals(piece))){
            // - vad daca nu raman in sah daca mut piesa :

            if(piece instanceof King) {
                // vreau sa vad daca nu se pune in sah singur
                int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};

                List<Piece> intersPieces = Piece.axesInters(this, to, dirs);

                for (Piece ps : intersPieces) {
                    if(ps != null && ps.getColor() != piece.getColor()){
                        if(ps instanceof King){
                            // check if the kings have enough distance between one another
                            if(Position.trajectory(ps.getPosition(), to).size() <= 2)
                                return false;
                        }
                        else {
                            // checks the possible moves for each of the opponent's pieces
                            if(ps.checkForCheck(this, to))
                                return false;

                        }
                    }
                }
            }
            else {
                // trasez 8 linii de la rege si vad daca se intersecteaza o
                // linie cu piesa asta pe care o am aici
                int[] dirs = {0, 1, 2, 3, 4, 5, 6, 7};


                List<Piece> intersPieces = Piece.axesInters(this, getKingPos(piece.getColor()),
                        dirs);

                if (intersPieces.contains(piece)) {
                    // incerc sa gasesc directia
                    int indDir = 0;
                    for (Piece ps : intersPieces) {
                        // aici inters pieces poate avea valori nule
                        if (ps != null && ps.equals(piece))
                            break;
                        indDir++;
                    }

                    // fac o directie si dupa parsez astfel incat sa vad ce e
                    // in spatele piesei
                    int[] auxDir = {indDir};
                    List<Piece> piesaDinSpate = Piece.axesInters(this, from, auxDir);

                    if (!piesaDinSpate.isEmpty() && piesaDinSpate.getFirst() != null) {

                        Piece piesadinSpate = piesaDinSpate.getFirst();

                        if (indDir == 0 || indDir == 2 || indDir == 4 || indDir == 6) {
                            if (piesadinSpate instanceof Bishop || piesadinSpate instanceof Queen)
                                return false;
                        } else if (piesadinSpate instanceof Rook) {
                            return false;
                        }

                    }
                }
            }
            // - sau daca esti deja in sah :
            List<ChessPair<Position, Piece>> checkingPieces = new ArrayList<ChessPair<Position, Piece>>();

            for(ChessPair<Position, Piece> ps : pieces)
                if(ps.getValue().getColor() != piece.getColor())
                    if(ps.getValue().checkForCheck(this, getKingPos(piece.getColor())))
                        checkingPieces.add(ps);

            if(!checkingPieces.isEmpty()){
                if(checkingPieces.size()>=2){
                    // aici poti misca doar regele
                    if(!(piece instanceof King))
                        return false;
                }
                else{
                    // daca pozitia to este pe traiectoria piesei atacante
                    // mutarea poate fi valida
                    Position kingPos = getKingPos(piece.getColor());
                    Position attackingPiecePos = checkingPieces.getFirst().getKey();

                    if(!Position.trajectory(attackingPiecePos, kingPos).contains(to) && !(piece instanceof King))
                        return false;
                }
            }

            // - daca e ceva acolo :
            if(getPieceAt(to) != null) {
                // trebuie sa vad daca e un pion mai intai si dupa
                // pentru ca daca se duce fix in fata nu poate lua ca orice alta piesa

                if(piece instanceof Pawn){
                    // trb sa vad directiile
                    int dir = Piece.dirFromPositions(from, to);

                    if(piece.getColor() == Colors.BLACK && dir == 3)
                        return false;

                    if(piece.getColor() == Colors.WHITE && dir == 7)
                        return false;
                }
                return getPieceAt(to).getColor() != piece.getColor();
            }

            return true;
        }
        return false;
    }

    public Position getKingPos(Colors color){
        for(ChessPair<Position, Piece> cp : pieces){
            Piece ps = (Piece)cp.getValue();
            if(ps instanceof King ){
                if(ps.getColor().equals(color)){
                    return cp.getKey();
                }
            }
        }
        return null;
    }

    public Piece initializeTypeOfPiece(String type, Colors color, Position pos) {
        return switch (type) {
            case "B" -> new Bishop(color, pos);
            case "K" -> new King(color, pos);
            case "N" -> new Knight(color, pos);
            case "P" -> new Pawn(color, pos);
            case "Q" -> new Queen(color, pos);
            case "R" -> new Rook(color, pos);
            default -> null;
        };
    }

    private void removePiece(ChessPair<Position, Piece> piece){
        // dau check de 2 ori din cauza unor bug-uri
        if(pieces.contains(piece)){
            pieces.remove(piece);
            return;
        }
        else{
            for(ChessPair<Position, Piece> ps : pieces){
                if(ps.getValue().equals(piece.getValue())){
                    pieces.remove(ps);
                    return;
                }
            }
        }
        throw new RuntimeException("Nu a deletat piesa din pieces");
    }

    public void main(String[] args){
        // - daca merge treesetul
        System.out.println("--------------------------TEST BOARD--------------------------");

        initialize();

        Piece piece = pieces.getFirst().getValue();

        Position a = new Position('D', 4);
        Position b = new Position('D', 3);

        System.out.println(Piece.dirFromPositions(a, b));

    }

    public String toString(Colors color){
        // trebuie sa afisez din perspectiva culorii

        String ceil = "   ------------------------------------\n";

        StringBuilder str = new StringBuilder();

        StringBuilder[] ans = new StringBuilder[11];

        ans[0]=new StringBuilder();
        ans[0].append(ceil);

        // mai intai pune fiecare piesa intr-un matrix pentru apelare usoara
        String[][] matrix = new String[9][9];

        if(Colors.WHITE == color) {

            for (ChessPair<Position, Piece> cp : pieces) {
                Position ps = cp.getKey();
                matrix[7 - (ps.y - 1)][ps.x - 'A'] = cp.getValue().toString();
            }

            for (int i = 0; i < 8; i++) {
                ans[i + 1] = new StringBuilder();
                ans[i + 1].append(" ").append(8 - i).append(" | ");
                for (int j = 0; j < 8; j++) {
                    if (matrix[i][j] != null)
                        ans[i + 1].append(matrix[i][j]).append(" ");
                    else
                        ans[i + 1].append("... ");
                }
                ans[i + 1].append("\n");
            }

            ans[9] = new StringBuilder();
            ans[9].append(ceil);

            String xString = "      A   B   C   D   E   F   G   H\n";

            ans[10] = new StringBuilder();
            ans[10].append(xString);
        }
        else{
            for (ChessPair<Position, Piece> cp : pieces) {
                Position ps = cp.getKey();
                matrix[ps.y - 1][7-(ps.x - 'A')] = cp.getValue().toString();
            }

            for (int i = 0; i < 8; i++) {
                ans[i + 1] = new StringBuilder();
                ans[i + 1].append(" ").append(i+1).append(" | ");
                for (int j = 0; j < 8; j++) {
                    if (matrix[i][j] != null)
                        ans[i + 1].append(matrix[i][j]).append(" ");
                    else
                        ans[i + 1].append("... ");
                }
                ans[i + 1].append("\n");
            }

            ans[9] = new StringBuilder();
            ans[9].append(ceil);

            String xString = "      H   G   F   E   D   C   B   A\n";

            ans[10] = new StringBuilder();
            ans[10].append(xString);
        }

        return Arrays.toString(ans);

    }

    public static int pointsFromCapture(Piece capturedPiece){
        int points = 0;
        switch (capturedPiece.type()) {
            case 'P' -> points = 10;
            case 'R' -> points = 50;
            case 'N' -> points = 30;
            case 'B' -> points = 30;
            case 'Q' -> points = 90;
        }
        return points;
    }
}
