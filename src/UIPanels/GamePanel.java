package UIPanels;

import Exceptions.InvalidMoveException;
import Pieces.Piece;
import UIPanels.GameObserver;
import Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements GameObserver {
    private JButton[][] squares;
    private Point selectedPiece;
    private JButton selectedButton;
    public Game currentGame;
    private JTextArea historyArea;
    private ArrayList<JButton> highlightedSquares;

    static String[] whitePieces = {"♖","♘","♗","♕","♔","♗","♘","♖","♙"};
    static String[] blackPieces = {"♜","♞","♝","♛","♚","♝","♞","♜","♟"};

    public GamePanel(Game game){

        // first update the current game
        currentGame = game;

        highlightedSquares = new ArrayList<>();

        // initialize the GamePanel
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0));

        // left sidebar : move History ------------------------------------
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(new Color(30, 40, 60));
        historyPanel.setPreferredSize(new Dimension(200, 0));
        historyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel histLabel = new JLabel("Move History");
        histLabel.setForeground(Color.WHITE);
        historyPanel.add(histLabel, BorderLayout.NORTH);

        historyArea = new JTextArea("Do this lil bro");
        historyArea.setBackground(new Color(30, 40, 60));
        historyArea.setForeground(new Color(148, 163, 184));
        historyPanel.add(historyArea, BorderLayout.CENTER);

        // ------------------------------------------------------------------------

        // make the board ---------------------------------------------------------
        JPanel boardWrapper = new JPanel(new GridBagLayout()); // Centers the board
        boardWrapper.setBackground(new Color(20, 25, 40));

        JPanel board = new JPanel(new GridLayout(8, 8));
        board.setPreferredSize(new Dimension(500, 500));

        boolean white = true;

        squares = new JButton[8][8];
        selectedPiece = null;
        selectedButton = null;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                squares[row][col] = square;
                if (white) square.setBackground(new Color(240, 217, 181));
                else square.setBackground(new Color(181, 136, 99));

                square.setBorderPainted(false);
                square.setFocusPainted(false);
                // square.setFont(new Font("Arial", Font.PLAIN, 10));

                // store the coordinates inside the button
                square.putClientProperty("x", col);
                square.putClientProperty("y", row);

                // add listener
                square.addActionListener(new PieceClickListener());

                board.add(square);
                white = !white;
            }
            white = !white;
        }
        boardWrapper.add(board);

        // ------------------------------------------------------------------------

        // right panel : captured pieces ------------------------------------
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(30, 40, 60));
        rightPanel.setPreferredSize(new Dimension(220, 0));
        rightPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel lblCap = new JLabel("Captured Pieces");
        lblCap.setForeground(Color.WHITE);

        JLabel capturedPiecesWhite = new JLabel("White Captured Pieces");
        JLabel capturedPiecesBlack = new JLabel("Black Captured Pieces");

        capturedPiecesWhite.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPiecesBlack.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(lblCap);
        rightPanel.add(new JLabel("White captured pieces : "));
        rightPanel.add(capturedPiecesWhite);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(new JLabel("Black captured pieces : "));
        rightPanel.add(capturedPiecesBlack);
        rightPanel.add(Box.createVerticalStrut(180)); // Spacer

        JButton btnResign = createButton("Resign", new Color(239, 68, 68));
        JButton btnSave = createButton("Save & Exit", Color.ORANGE);
        JButton btnBack = createButton("Back to Menu", Color.GRAY);

        btnBack.addActionListener(e -> MainFrame.showCard("MENU"));

        // Full width buttons
        btnResign.setMaximumSize(new Dimension(200, 40));
        btnSave.setMaximumSize(new Dimension(200, 40));
        btnBack.setMaximumSize(new Dimension(200, 40));

        rightPanel.add(btnResign);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnSave);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnBack);

        //------------------------------------------------------------------------

        add(historyPanel, BorderLayout.WEST);
        add(boardWrapper, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

    }
    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }


    // class for a piece click
    private class PieceClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            JButton piece = (JButton) e.getSource();

            int x = (int) piece.getClientProperty("x");
            int y = (int) piece.getClientProperty("y");

            Colors c1 = getColorFromXY(x,y);
            Colors c2 = getCurrentPlayerFromInd(currentGame.currentPlayerInd).pieceColor;

            if(c1 != c2 && selectedPiece == null)
                return;

            // check how many times the user clicked
            if (selectedPiece == null) {
                // here the user selected a piece

                // first check if there is a piece there and not an empty square
                if (!piece.getText().isEmpty()) {
                    selectedPiece = new Point(x, y);
                    selectedButton = piece;
                    onPieceSelected(selectedPiece);
                    piece.setBackground(Color.GRAY);
                    highlightedSquares.add(piece);

                }

            }
            else {
                // here the user wants to make a move

                // !! important
                // in highlightedSquares are the valid moves of the selected piece
                // the code that follows assumes this

                // make the move :
                // first check if it is from the highlighted moves and not the piece
                // I previously selected
                if(highlightedSquares.contains(piece) && piece.getBackground() != Color.GRAY){

                    // handle the backend :

                    //!! vezi cum sunt coordonatele
                    Colors color = getColorFromXY(selectedPiece.y + 1, selectedPiece.x);
                    Position from = new Position((char) ('A' + selectedPiece.x), selectedPiece.y + 1);
                    Position to = new Position((char) ('A' + x), y +1);

                    Move move = new Move(color, from, to);

                    onMoveMade(move);

                    onPlayerSwitch();

                    // handle the frontend/ visual :
                    updatePiecesVisual(currentGame.getBoard());

                    selectedButton.setText("");
                    selectedButton = null;

                    System.out.println("Moved to: " + x + "," + y);
                }

                // reset selectedPiece
                selectedPiece = null;

                // reset the highlighted squares
                resetColor();
            }

        }

    }

    private Colors getColorFromXY(int x, int y){
        JButton btn = squares[y][x];

        String str = btn.getText();

        for(String s : whitePieces)
            if(str.equals(s))
                return Colors.WHITE;

        for(String s : blackPieces)
            if(str.equals(s))
                return Colors.BLACK;

        return Colors.GRAY;
    }

    // resets the highlighted squares
    private void resetColor() {
        for(JButton jb : highlightedSquares){
            int r = (int) jb.getClientProperty("x");
            int c = (int) jb.getClientProperty("y");

            if ((r + c) % 2 == 0) {
                squares[c][r].setBackground(new Color(240, 217, 181));
            } else {
                squares[c][r].setBackground(new Color(181, 136, 99));
            }
        }
        highlightedSquares.clear();
    }

    // class for the menu button
    private class MenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // when the user wants to return to the menu
            onMenuButtonClicked();
        }
    }

    @Override
    public void onMenuButtonClicked() {
        // TODO : implement logic for exit
    }

    @Override
    public void onPlayerSwitch() {
        currentGame.switchPlayer();
    }

    public Player getCurrentPlayerFromInd(int currentPlayerInd){
        Player curPlayer = null;
        if(currentGame.currentPlayerInd % 2 == 1){
            if(currentGame.getPlayer().pieceColor == Colors.WHITE)
                curPlayer = currentGame.getPlayer();
            else curPlayer = currentGame.getOpponent();
        }
        else{
            if(currentGame.getPlayer().pieceColor == Colors.WHITE)
                curPlayer = currentGame.getOpponent();
            else curPlayer = currentGame.getPlayer();
        }
        return curPlayer;
    }

    @Override
    public void onMoveMade(Move move) {
        currentGame.handleMove(move);

        if(currentGame.checkForCheckMate(getCurrentPlayerFromInd(currentGame.currentPlayerInd))){
            currentGame.handleEndOfGame(2);
        }
    }

    @Override
    public void onPieceCaptured(Piece piece) {
        updateCapturedPieces();
    }

    @Override
    public void onPieceSelected(Point point) {
        // get the piece from the point and highlight on the board
        // where each possible move is; put them all in highlightedSquares

        resetColor();

        Position pos = new Position((char)('A' + point.x), point.y + 1);
        Piece ps = currentGame.getBoard().getPieceAt(pos);

        // check first if the square contains the color that the player is in
        if(ps.getColor() != getCurrentPlayerFromInd(currentGame.currentPlayerInd).pieceColor)
            return;

        List<Position> posMoves = ps.getPossibleMoves(currentGame.getBoard());

        for(Position p : posMoves){
            JButton btn = squares[p.y-1][p.x-'A'];
            btn.setBackground(Color.LIGHT_GRAY);
            highlightedSquares.add(btn);
        }
    }

    public void updatePiecesVisual(Board board){

        if(currentGame.getPlayer().pieceColor == Colors.WHITE) {
            for(int row = 0 ; row <8; row++) {
                for(int col = 0; col <8; col++) {
                    Position pos = new Position((char)('A' + 7 - col),  8 - row);
                    Piece ps = board.getPieceAt(pos);
                    if(ps == null)
                        continue;

                    String str = getStrFromPiece(ps);
                    squares[row][col].setText(str);

                }
            }
        } else{
            for(int row = 0 ; row <8; row++) {
                for(int col = 0; col <8; col++) {
                    Position pos = new Position((char)('A' + col),  row +1);
                    Piece ps = board.getPieceAt(pos);
                    if(ps == null)
                        continue;

                    String str = getStrFromPiece(ps);
                    squares[row][col].setText(str);

                }
            }
        }


    }

    public void updateCapturedPieces(){
        // TODO : update captured pieces on capture



    }

    public void updateHistoryArea(){
        // TODO : update history area

    }

    public String getStrFromPiece(Piece piece){

        if(piece == null)
            return null;

        if(piece.getColor() == Colors.WHITE){
            switch (piece.type()){
                case 'N':
                    return whitePieces[1];
                case 'R':
                    return whitePieces[0];
                case 'B':
                    return whitePieces[2];
                case 'Q':
                    return whitePieces[3];
                case 'K':
                    return whitePieces[4];
                case 'P':
                    return whitePieces[8];
            }
        }
        else{
            switch (piece.type()){
                case 'N':
                    return blackPieces[1];
                case 'R':
                    return blackPieces[0];
                case 'B':
                    return blackPieces[2];
                case 'Q':
                    return blackPieces[3];
                case 'K':
                    return blackPieces[4];
                case 'P':
                    return blackPieces[8];
            }
        }
        return null;
    }
}
