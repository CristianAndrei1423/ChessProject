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
    public JLabel endOfGameLabelState;
    private ArrayList<JButton> highlightedSquares;
    public boolean isWhiteView; // Field to store board orientation

    static String[] whitePieces = {"♖","♘","♗","♕","♔","♗","♘","♖","♙"};
    static String[] blackPieces = {"♜","♞","♝","♛","♚","♝","♞","♜","♟"};

    static ImageIcon[] whiteIcons;
    static ImageIcon[] blackIcons;
    static ImageIcon empty;

    public GamePanel(Game game){

        // first update the current game
        currentGame = game;

        highlightedSquares = new ArrayList<>();

        // initalize the icons
        initIcons();

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

        btnSave.addActionListener(new SaveAndExitListener());
        btnResign.addActionListener(new ForfeitListener());

        // Full width buttons
        btnResign.setMaximumSize(new Dimension(200, 40));
        btnSave.setMaximumSize(new Dimension(200, 40));
        //btnBack.setMaximumSize(new Dimension(200, 40));

        endOfGameLabelState = new JLabel();
        endOfGameLabelState.setVisible(false);

        rightPanel.add(endOfGameLabelState);
        rightPanel.add(Box.createVerticalStrut(10));
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

            //Colors c1 = getColorFromXY(x,y);
            Colors c2 = currentGame.currentPlayerColor;

            Piece ps = currentGame.getBoard().getPieceAt(new Position((char)('H' - x), y + 1));
            if(isWhiteView)
                ps = currentGame.getBoard().getPieceAt(new Position((char)('A' + x), 8 - y));

            if(ps != null){
                Colors c1 = ps.getColor();
                if(c1 == c2 && selectedPiece == null) {
                    if (!piece.getIcon().equals(empty)) {
                        selectedPiece = new Point(x, y);
                        selectedButton = piece;
                        onPieceSelected(selectedPiece);
                        piece.setBackground(Color.GRAY);
                        highlightedSquares.add(piece);
                    }
                    return;
                }
            }

            // check how many times the user clicked
            if(selectedPiece != null){
                if(highlightedSquares.contains(piece) && piece != selectedButton){
                    Position from, to;
                    // convert button coords to board coords
                    if(isWhiteView) {
                        from = new Position((char) ('A' + selectedPiece.x), 8 - selectedPiece.y);
                        to = new Position((char) ('A' + x), 8 - y);
                    } else {
                        from = new Position((char) ('H' - selectedPiece.x), selectedPiece.y + 1);
                        to = new Position((char) ('H' - x), y + 1);
                    }

                    Move move = new Move(c2, from, to);
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

            if(!currentGame.gameStillValid){
                // the game ended lil bro
                if(!endOfGameLabelState.isVisible())
                    Main.getChessGame().handleEndGame(currentGame);
            }

        }

    }

    private class SaveAndExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // AFAI this works
            currentGame.handleExitGame(currentGame);
            MainFrame.showCard("MENU");
        }
    }

    private class ForfeitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentGame.handleEndOfGame(-1);
            currentGame.handleExitGame(currentGame);
            MainFrame.showCard("MENU");
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

    private void initIcons(){
        whiteIcons = new ImageIcon[9];
        blackIcons = new ImageIcon[9];

        whiteIcons[0] = new ImageIcon("src/ChessImages/WhiteRook.png");
        whiteIcons[1] = new ImageIcon("src/ChessImages/WhiteKnight.png");
        whiteIcons[2] = new ImageIcon("src/ChessImages/WhiteBishop.png");
        whiteIcons[3] = new ImageIcon("src/ChessImages/WhiteQueen.png");
        whiteIcons[4] = new ImageIcon("src/ChessImages/WhiteKing.png");
        whiteIcons[8] = new ImageIcon("src/ChessImages/WhitePawn.png");
        whiteIcons[5] = whiteIcons[2];
        whiteIcons[6] = whiteIcons[1];
        whiteIcons[7] = whiteIcons[0];

        blackIcons[0] = new ImageIcon("src/ChessImages/BlackRook.png");
        blackIcons[1] = new ImageIcon("src/ChessImages/BlackKnight.png");
        blackIcons[2] = new ImageIcon("src/ChessImages/BlackBishop.png");
        blackIcons[3] = new ImageIcon("src/ChessImages/BlackQueen.png");
        blackIcons[4] = new ImageIcon("src/ChessImages/BlackKing.png");
        blackIcons[8] = new ImageIcon("src/ChessImages/BlackPawn.png");
        blackIcons[5] = blackIcons[2];
        blackIcons[6] = blackIcons[1];
        blackIcons[7] = blackIcons[0];

        empty = new ImageIcon("src/ChessImages/Empty.png");
    }

    @Override
    public void onPlayerSwitch() {
        // here whenever the player makes a move
        // it should automatically handle the computer making a move
        currentGame.switchPlayer();

        if(currentGame.currentPlayerColor == currentGame.getOpponent().pieceColor){
            // this means it's the computer's round
            // currentGame.runForComputer(); // -- returns true if game goes on TODO

            if(!currentGame.runForComputer()) {
                // game ended
                System.out.println("Player won");

                // don't switch player, so that the player can't do any more moves
                return;
            }

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

        posMoves = ps.getPossibleMoves(currentGame.getBoard());

        for(Position p : posMoves){
            // Convert Backend Position to UI Grid [Row][Col]
            int uiRow, uiCol;
            if(isWhiteView) {
                uiRow = 8 - p.y;
                uiCol = p.x - 'A';
            } else {
                uiRow = p.y - 1;
                uiCol = 'H' - p.x;
            }
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
                    pos = new Position((char)('A' + col), 8 - row);
                } else {
                    pos = new Position((char)('H' - col), row + 1);
                }

                Piece ps = board.getPieceAt(pos);

                if(ps == null) {
                    squares[row][col].setIcon(empty);
                } else {
                    squares[row][col].setIcon(getIconFromPiece(ps));
                }
            }
        }

        // System.out.println(currentGame.getBoard().toString(Colors.WHITE));

    }

    public void updateCapturedPieces(){
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

    public ImageIcon getIconFromPiece(Piece piece){

        if(piece == null)
            return null;

        if(piece.getColor() == Colors.WHITE){
            switch (piece.type()){
                case 'N': return whiteIcons[1];
                case 'R': return whiteIcons[0];
                case 'B': return whiteIcons[2];
                case 'Q': return whiteIcons[3];
                case 'K': return whiteIcons[4];
                case 'P': return whiteIcons[8];
            }
        }
        else{
            switch (piece.type()){
                case 'N': return blackIcons[1];
                case 'R': return blackIcons[0];
                case 'B': return blackIcons[2];
                case 'Q': return blackIcons[3];
                case 'K': return blackIcons[4];
                case 'P': return blackIcons[8];
            }
        }
        return empty;
    }
}