package Utils;

import Pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class Board {

    TreeSet<ChessPair<Position, Piece>> pieces;

    public Board(){
        this.pieces = new TreeSet<>();
    }

    public Board(TreeSet<ChessPair<Position, Piece>> pieces){
        this.pieces = pieces;
    }

    public void initialize(){
        // initialize board with pieces and add them to internal list

        // ensure position in Piece is the same as in ChessPair
        Piece rookB1 = initializeTypeOfPiece("R", Colors.BLACK, new Position('A', 8));
        pieces.add(new ChessPair<>(rookB1.getPosition(), rookB1));
        Piece knightB1 = initializeTypeOfPiece("N", Colors.BLACK, new Position('B', 8));
        pieces.add(new ChessPair<>(knightB1.getPosition(), knightB1));
        Piece bishopB1 = initializeTypeOfPiece("B", Colors.BLACK, new Position('C', 8));
        pieces.add(new ChessPair<>(bishopB1.getPosition(), bishopB1));
        Piece queenB = initializeTypeOfPiece("Q", Colors.BLACK, new Position('D', 8));
        pieces.add(new ChessPair<>(queenB.getPosition(), queenB));
        Piece kingB = initializeTypeOfPiece("K", Colors.BLACK, new Position('E', 8));
        pieces.add(new ChessPair<>(kingB.getPosition(), kingB));
        Piece bishopB2 = initializeTypeOfPiece("B", Colors.BLACK, new Position('F', 8));
        pieces.add(new ChessPair<>(bishopB2.getPosition(), bishopB2));
        Piece knightB2 = initializeTypeOfPiece("N", Colors.BLACK, new Position('G', 8));
        pieces.add(new ChessPair<>(knightB2.getPosition(), knightB2));
        Piece rookB2 = initializeTypeOfPiece("R", Colors.BLACK, new Position('H', 8));
        pieces.add(new ChessPair<>(rookB2.getPosition(), rookB2));

        Piece[] pawnsB = new Piece[8];

        for (int i = 0; i < 8; i++) {
            char aux = (char) ((int) 'A' + i);
            pawnsB[i] = initializeTypeOfPiece("P", Colors.BLACK, new Position(aux, 7));
            pieces.add(new ChessPair<>(pawnsB[i].getPosition(), pawnsB[i]));
        }

        Piece rookW1 = initializeTypeOfPiece("R", Colors.WHITE, new Position('A', 1));
        pieces.add(new ChessPair<>(rookW1.getPosition(), rookW1));
        Piece knightW1 = initializeTypeOfPiece("N", Colors.WHITE, new Position('B', 1));
        pieces.add(new ChessPair<>(knightW1.getPosition(), knightW1));
        Piece bishopW1 = initializeTypeOfPiece("B", Colors.WHITE, new Position('C', 1));
        pieces.add(new ChessPair<>(bishopW1.getPosition(), bishopW1));
        Piece queenW = initializeTypeOfPiece("Q", Colors.WHITE, new Position('D', 1));
        pieces.add(new ChessPair<>(queenW.getPosition(), queenW));
        Piece kingW = initializeTypeOfPiece("K", Colors.WHITE, new Position('E', 1));
        pieces.add(new ChessPair<>(kingW.getPosition(), kingW));
        Piece bishopW2 = initializeTypeOfPiece("B", Colors.WHITE, new Position('F', 1));
        pieces.add(new ChessPair<>(bishopW2.getPosition(), bishopW2));
        Piece knightW2 = initializeTypeOfPiece("N", Colors.WHITE, new Position('G', 1));
        pieces.add(new ChessPair<>(knightW2.getPosition(), knightW2));
        Piece rookW2 = initializeTypeOfPiece("R", Colors.WHITE, new Position('H', 1));
        pieces.add(new ChessPair<>(rookW2.getPosition(), rookW2));
        Piece[] pawnsW = new Piece[8];

        for (int i = 0; i < 8; i++) {
            char aux = (char) ((int) 'A' + i);
            pawnsW[i] = initializeTypeOfPiece("P", Colors.WHITE, new Position(aux, 2));
            pieces.add(new ChessPair<>(pawnsW[i].getPosition(), pawnsW[i]));
        }
    }

    private void handleCaptureInternal(Player player, Player op, Position to){
        System.out.println("Updated player points");
        // update player points
        player.setPoints(player.getPoints() + pointsFromCapture(getPieceAt(to)));

        // update captured pieces of the player
        player.addCapturedPiece(getPieceAt(to));

        // update captured piece on board and on pieces of the opponent
        ChessPair<Position, Piece> ps = new ChessPair<>(to, getPieceAt(to));

        System.out.println("Remove captured piece from board");
        // remove captured piece from board
        removePiece(ps);

        System.out.println("Remove captured piece from opponent owned piece");
        op.removeOwnedPiece(ps);
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
                            // update on board
                            Piece Queen = initializeTypeOfPiece("Q",
                                    pair.getValue().getColor(), pair.getValue().getPosition());
                            // update in Player owned pieces
                            for(ChessPair<Position, Piece> pairs : player.getOwnedPieces())
                                if(pairs.getKey().equals(pair.getKey()))
                                    pairs.setValue(Queen);

                            pair.setValue(Queen);

                            piece = Queen;
                        }
                        else if(pair.getValue().getColor().equals(Colors.WHITE) && to.y == 8){
                            // update on board
                            Piece Queen = initializeTypeOfPiece("Q",
                                    pair.getValue().getColor(), pair.getValue().getPosition());

                            // update in Player owned pieces
                            for(ChessPair<Position, Piece> pairs : player.getOwnedPieces())
                                if(pairs.getKey().equals(pair.getKey()))
                                    pairs.setValue(Queen);

                            pair.setValue(Queen);
                            piece = Queen;
                        }

                    }

                    // check if move results in capture
                    if(getPieceAt(to) != null && getPieceAt(to).getColor() != pair.getValue().getColor()){
                        // check if it is a pawn and if it is, check if move results in capture
                        if(pair.getValue() instanceof Pawn){
                            int dirOfMove = Piece.dirFromPositions(from, to);
                            if(pair.getValue().getColor() == Colors.BLACK){
                                if(dirOfMove == 2 || dirOfMove == 4){
                                    handleCaptureInternal(player, op, to);
                                }
                            } else {
                                if(dirOfMove == 0 || dirOfMove == 6){
                                    handleCaptureInternal(player, op, to);
                                }
                            }
                        }
                        else {
                            handleCaptureInternal(player, op, to);
                        }
                    }

                    // update the position of the moved piece
                    piece.setPosition(to);

                    System.out.println("Removed old pair " + pair.getKey().toString() + " " + pair.getValue().toString());
                    player.removeOwnedPiece(pair);

                    pieces.remove(pair);
                    pair.setKey(to);
                    pieces.add(pair);

                    System.out.println("Added new pair " + to + " " + pair.getValue().toString());
                    player.addOwnedPiece(new ChessPair<>(to, pair.getValue()));

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

    public Position getKingPos(Colors color){
        for(ChessPair<Position, Piece> cp : pieces){
            Piece ps = cp.getValue();
            if(ps instanceof King ){
                if(ps.getColor().equals(color)){
                    return cp.getKey();
                }
            }
        }
        return null;
    }

    public boolean isValidMove(Position from, Position to, Piece piece) {
        // redone this function from scratch
        // now simulate the move being made and then see if it's valid

        // first check if the move and piece exist
        if (piece == null) return false;
        if (!to.onBoard()) return false;

        // check if it's your piece
        Piece target = getPieceAt(to);
        if (target != null && target.getColor() == piece.getColor()) {
            return false;
        }

        // temporary move on board
        Position originalPos = piece.getPosition();
        ChessPair<Position, Piece> originalPair = new ChessPair<>(from, piece);
        ChessPair<Position, Piece> targetPair = null;

        // temp remove the piece from its current position
        boolean removed = false;
        for (ChessPair<Position, Piece> cp : pieces) {
            if (cp.getKey().equals(from) && cp.getValue().equals(piece)) {
                pieces.remove(cp);
                // reference for later
                originalPair = cp;
                removed = true;
                break;
            }
        }
        // redundant but let's be sure
        if (!removed) return false;

        // if this is a capture remove the captured piece temporarily
        if (target != null) {
            for (ChessPair<Position, Piece> cp : pieces) {
                if (cp.getKey().equals(to)) {
                    pieces.remove(cp);
                    // reference for later
                    targetPair = cp;
                    break;
                }
            }
        }

        // place the moving piece at the new position
        piece.setPosition(to);
        pieces.add(new ChessPair<>(to, piece));

        // check if the king is safe after this move
        boolean isKingSafe = true;
        // try finally block because it needs to revert even if it results in error

        try {
            Position kingPos = getKingPos(piece.getColor());

            //check if it's under attack
            if (kingPos != null) {
                for (ChessPair<Position, Piece> cp : pieces) {
                    Piece enemy = cp.getValue();
                    // if it's an enemy piece, check if it attacks the king
                    if (enemy.getColor() != piece.getColor()) {
                        if (enemy.checkForCheck(this, kingPos)) {
                            // it is checked
                            isKingSafe = false;
                            break;
                        }
                    }
                }
            }
        } finally {
            // revert the board state

            // remove the piece from the treeset
            pieces.remove(new ChessPair<>(to, piece));

            // restore position
            piece.setPosition(originalPos);

            // add back to from
            pieces.add(originalPair);

            // add back the captured piece (if any)
            if (targetPair != null) pieces.add(targetPair);
        }

        return isKingSafe;
    }

    // factory pattern
    public static Piece initializeTypeOfPiece(String type, Colors color, Position pos) {
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
        // check 2 times because of bugz
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
        throw new RuntimeException("It did not remove the piece");
    }

    public void main(){
        // test to see if the treeset works
        System.out.println("--------------------------TEST BOARD--------------------------");

        initialize();

        Position a = new Position('D', 4);
        Position b = new Position('D', 3);

        System.out.println(Piece.dirFromPositions(a, b));

    }

    /// deprecated but useful for testing, shows whole board in ascii
    public String toString(Colors color){

        String ceil = "   ------------------------------------\n";

        StringBuilder[] ans = new StringBuilder[11];

        ans[0]=new StringBuilder();
        ans[0].append(ceil);

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
