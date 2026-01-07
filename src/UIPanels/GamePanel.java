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
    private JLabel capturedPiecesWhite;
    private JLabel capturedPiecesBlack;
    private ArrayList<JButton> highlightedSquares;
    public boolean isWhiteView; // Field to store board orientation

    static String[] whitePieces = {"♖","♘","♗","♕","♔","♗","♘","♖","♙"};
    static String[] blackPieces = {"♜","♞","♝","♛","♚","♝","♞","♜","♟"};

    public GamePanel(Game game){

        // first update the current game
        currentGame = game;

        // Determine board orientation based on the player's color
        // this.isWhiteView = currentGame.getPlayer().pieceColor == Colors.WHITE;

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

        historyArea = new JTextArea("Game Started");
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

        // Swing Grid Layout: Row 0 is TOP, Row 7 is BOTTOM
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                squares[row][col] = square;
                if (white) square.setBackground(new Color(240, 217, 181));
                else square.setBackground(new Color(181, 136, 99));

                square.setBorderPainted(false);
                square.setFocusPainted(false);
                // square.setFont(new Font("SansSerif", Font.PLAIN, 40));

                // store the coordinates inside the button
                // x = column (visual), y = row (visual)
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

        capturedPiecesWhite = new JLabel("White Captured Pieces");
        capturedPiecesBlack = new JLabel("Black Captured Pieces");

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
        //JButton btnBack = createButton("Back to Menu", Color.GRAY);

        btnSave.addActionListener(e -> MainFrame.showCard("MENU"));

        // Full width buttons
        btnResign.setMaximumSize(new Dimension(200, 40));
        btnSave.setMaximumSize(new Dimension(200, 40));
        //btnBack.setMaximumSize(new Dimension(200, 40));

        rightPanel.add(btnResign);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnSave);
        //rightPanel.add(Box.createVerticalStrut(10));
        //rightPanel.add(btnBack);

        //------------------------------------------------------------------------

        add(historyPanel, BorderLayout.WEST);
        add(boardWrapper, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Initial Visual Update
        // updatePiecesVisual(currentGame.getBoard());
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
            Colors c2 = currentGame.currentPlayerColor;

            // If selecting a piece of valid color
            // Logic: Color of piece (c1) must match Current Player's color (c2)
            if(c1 == c2 && selectedPiece == null) {
                if (!piece.getText().isEmpty()) {
                    selectedPiece = new Point(x, y);
                    selectedButton = piece;
                    onPieceSelected(selectedPiece);
                    piece.setBackground(Color.GRAY);
                    highlightedSquares.add(piece);
                }
                return;
            }

            // check how many times the user clicked
            if(selectedPiece != null){
                if(highlightedSquares.contains(piece) && piece != selectedButton){

                    Colors color = c2;
                    Position from, to;

                    // Convert Visual Coords (x,y) to Backend Positions based on View
                    if(isWhiteView) {
                        // White View: Row 0 is Rank 8, Col 0 is A
                        from = new Position((char) ('A' + selectedPiece.x), 8 - selectedPiece.y);
                        to = new Position((char) ('A' + x), 8 - y);
                    } else {
                        // Black View: Row 0 is Rank 1, Col 0 is H (Board is rotated 180)
                        from = new Position((char) ('H' - selectedPiece.x), selectedPiece.y + 1);
                        to = new Position((char) ('H' - x), y + 1);
                    }

                    Move move = new Move(color, from, to);
                    onMoveMade(move);
                    onPlayerSwitch();

                    // handle the frontend/ visual :
                    updatePiecesVisual(currentGame.getBoard());

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
        JButton btn = squares[y][x]; // squares is [row][col]

        String str = btn.getText();
        if(str == null || str.isEmpty()) return Colors.GRAY;

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
            int c = (int) jb.getClientProperty("x");
            int r = (int) jb.getClientProperty("y");

            if ((r + c) % 2 == 0) {
                squares[r][c].setBackground(new Color(240, 217, 181));
            } else {
                squares[r][c].setBackground(new Color(181, 136, 99));
            }
        }
        // Also reset the selected piece color if it exists
        if(selectedButton != null) {
            int c = (int) selectedButton.getClientProperty("x");
            int r = (int) selectedButton.getClientProperty("y");
            if ((r + c) % 2 == 0) {
                selectedButton.setBackground(new Color(240, 217, 181));
            } else {
                selectedButton.setBackground(new Color(181, 136, 99));
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
        // here whenever the player makes a move
        // it should automatically handle the computer making a move
        currentGame.switchPlayer();

        if(currentGame.currentPlayerColor == currentGame.getOpponent().pieceColor){
            // this means it's the computer's round
            currentGame.runForComputer(); // -- returns true if game goes on TODO
            updatePiecesVisual(currentGame.getBoard());
            currentGame.switchPlayer();
        }

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

        // if the move results in a capture, update the right panel
        updateCapturedPieces();

        // update history in left panel
        updateHistoryArea();

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
        resetColor();

        Position pos;

        // Convert UI Point to Backend Position
        if(isWhiteView) {
            pos = new Position((char)('A' + point.x), 8 - point.y);
        } else {
            pos = new Position((char)('H' - point.x), point.y + 1);
        }

        Piece ps = currentGame.getBoard().getPieceAt(pos);
        if (ps == null) return;

        if(ps.getColor() != currentGame.currentPlayerColor)
            return;

        List<Position> posMoves = ps.getPossibleMoves(currentGame.getBoard());

        for(Position p : posMoves){
            // Convert Backend Position to UI Grid [Row][Col]
            int uiRow, uiCol;

            if(isWhiteView) {
                uiRow = 8 - p.y;
                uiCol = p.x - 'A';
            } else {
                uiRow = p.y - 1; // Rank 1 is at Row 0
                uiCol = 'H' - p.x; // File H is at Col 0
            }

            // Bounds check
            if(uiRow >= 0 && uiRow < 8 && uiCol >= 0 && uiCol < 8) {
                JButton btn = squares[uiRow][uiCol];
                btn.setBackground(Color.LIGHT_GRAY);
                highlightedSquares.add(btn);
            }
        }
    }

    public void updatePiecesVisual(Board board){
        // Loop through the VISUAL grid (0,0 is Top-Left)
        for(int row = 0 ; row < 8; row++) {
            for(int col = 0; col < 8; col++) {

                Position pos;
                if(isWhiteView) {
                    // White View: Row 0 = Rank 8, Col 0 = A
                    pos = new Position((char)('A' + col), 8 - row);
                } else {
                    // Black View: Row 0 = Rank 1, Col 0 = H
                    pos = new Position((char)('H' - col), row + 1);
                }

                Piece ps = board.getPieceAt(pos);

                if(ps == null) {
                    squares[row][col].setText("");
                } else {
                    String str = getStrFromPiece(ps);
                    squares[row][col].setText(str);
                }
            }
        }
    }

    public void updateCapturedPieces(){
        // TODO : update captured pieces on capture
        // update captured pieces based on each player's captured pieces

        Colors playerCol = currentGame.getPlayer().pieceColor;
        Colors compCol = currentGame.getOpponent().pieceColor;

        // first check if a piece was captured -- DEPRECATED, USE OBSERVER
        if(currentGame.getPlayer().nrofCapturedPieces != currentGame.getPlayer().getCapturedPieces().size()){
            currentGame.getPlayer().nrofCapturedPieces = currentGame.getPlayer().getCapturedPieces().size();

            String pieceStr = getStrFromPiece(currentGame.getPlayer().getCapturedPieces().getLast());

            // change the captured pieces label
            if(playerCol == Colors.WHITE){
                capturedPiecesWhite.setText(capturedPiecesWhite.getText() + pieceStr);
            } else {
                capturedPiecesBlack.setText(capturedPiecesBlack.getText() + pieceStr);
            }
        }

        if(currentGame.getOpponent().nrofCapturedPieces != currentGame.getOpponent().getCapturedPieces().size()){
            currentGame.getOpponent().nrofCapturedPieces = currentGame.getOpponent().getCapturedPieces().size();

            String pieceStr = getStrFromPiece(currentGame.getOpponent().getCapturedPieces().getLast());

            // change the captured pieces label
            if(compCol == Colors.WHITE){
                capturedPiecesWhite.setText(capturedPiecesWhite.getText() + pieceStr);
            } else {
                capturedPiecesBlack.setText(capturedPiecesBlack.getText() + pieceStr);
            }
        }

    }

    public void updateHistoryArea(){
        // take each move in moveList from game and update each time a new move is done
        String str = historyArea.getText();

        Move mv = currentGame.getMoveList().getLast();

        historyArea.setText(str + "\n" + mv.toString());
    }

    public String getStrFromPiece(Piece piece){

        if(piece == null)
            return null;

        if(piece.getColor() == Colors.WHITE){
            switch (piece.type()){
                case 'N': return whitePieces[1];
                case 'R': return whitePieces[0];
                case 'B': return whitePieces[2];
                case 'Q': return whitePieces[3];
                case 'K': return whitePieces[4];
                case 'P': return whitePieces[8];
            }
        }
        else{
            switch (piece.type()){
                case 'N': return blackPieces[1];
                case 'R': return blackPieces[0];
                case 'B': return blackPieces[2];
                case 'Q': return blackPieces[3];
                case 'K': return blackPieces[4];
                case 'P': return blackPieces[8];
            }
        }
        return null;
    }
}