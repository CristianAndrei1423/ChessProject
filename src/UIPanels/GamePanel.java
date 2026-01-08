package UIPanels;

import Pieces.Piece;
import Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements GameObserver {
    private final JButton[][] squares;
    private Point selectedPiece;
    private JButton selectedButton;
    public Game currentGame;
    private final JTextArea historyArea;
    private final JLabel capturedPiecesWhite;
    private final JLabel capturedPiecesBlack;
    public JLabel endOfGameLabelState;
    private final ArrayList<JButton> highlightedSquares;
    public boolean isWhiteView;

    static String[] whitePieces = {"♖","♘","♗","♕","♔","♗","♘","♖","♙"};
    static String[] blackPieces = {"♜","♞","♝","♛","♚","♝","♞","♜","♟"};

    static ImageIcon[] whiteIcons;
    static ImageIcon[] blackIcons;
    static ImageIcon empty;

    public GamePanel(Game game){

        // first update the current game
        currentGame = game;

        highlightedSquares = new ArrayList<>();

        // initialize the icons
        initIcons();

        // initialize the GamePanel
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0));

        // left sidebar : move history ------------------------------------
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
        JPanel boardWrapper = new JPanel(new GridBagLayout());
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
        JPanel rightPanel = new JPanel(new BorderLayout()); // Use BorderLayout to allow pinning to bottom
        rightPanel.setBackground(new Color(30, 40, 60));
        rightPanel.setPreferredSize(new Dimension(220, 0));
        rightPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(30, 40, 60));

        JLabel lblCap = new JLabel("Captured Pieces");
        lblCap.setForeground(Color.WHITE);

        capturedPiecesWhite = new JLabel("White Captured Pieces");
        capturedPiecesBlack = new JLabel("Black Captured Pieces");

        capturedPiecesWhite.setAlignmentX(Component.LEFT_ALIGNMENT);
        capturedPiecesBlack.setAlignmentX(Component.LEFT_ALIGNMENT);

        endOfGameLabelState = new JLabel();
        endOfGameLabelState.setVisible(false);
        endOfGameLabelState.setForeground(Color.YELLOW);

        infoPanel.add(lblCap);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(new JLabel("White captured pieces : "));
        infoPanel.add(capturedPiecesWhite);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(new JLabel("Black captured pieces : "));
        infoPanel.add(capturedPiecesBlack);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(endOfGameLabelState);
        infoPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(2, 1, 0, 10));
        btnPanel.setBackground(new Color(30, 40, 60));

        JButton btnResign = createButton("Resign", new Color(239, 68, 68));
        JButton btnSave = createButton("Save & Exit", Color.ORANGE);

        btnSave.addActionListener(new SaveAndExitListener());
        btnResign.addActionListener(new ForfeitListener());

        btnPanel.add(btnResign);
        btnPanel.add(btnSave);

        rightPanel.add(btnPanel, BorderLayout.SOUTH);

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
            // if game is over, do not process clicks unless it's to trigger end game handling once
            if(!currentGame.gameStillValid){
                if(!endOfGameLabelState.isVisible())
                    Main.getChessGame().handleEndGame(currentGame);
                return;
            }

            JButton piece = (JButton) e.getSource();

            int x = (int) piece.getClientProperty("x");
            int y = (int) piece.getClientProperty("y");

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

            // Check game status after move and possible computer response
            if(!currentGame.gameStillValid){
                // the game ended
                if(!endOfGameLabelState.isVisible())
                    Main.getChessGame().handleEndGame(currentGame);
            }
        }
    }

    private class SaveAndExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // works
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

    // resets the highlighted squares
    private void resetColor() {
        for(JButton jb : highlightedSquares){
            int c = (int) jb.getClientProperty("x");
            int r = (int) jb.getClientProperty("y");

            if ((r + c) % 2 == 0) squares[r][c].setBackground(new Color(240, 217, 181));
            else squares[r][c].setBackground(new Color(181, 136, 99));
        }
        // reset the selected piece color if it exists
        if(selectedButton != null) {
            int c = (int) selectedButton.getClientProperty("x");
            int r = (int) selectedButton.getClientProperty("y");
            if ((r + c) % 2 == 0) selectedButton.setBackground(new Color(240, 217, 181));
            else selectedButton.setBackground(new Color(181, 136, 99));
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

            if(!currentGame.runForComputer()) {
                // game ended
                System.out.println("Computer finished turn with End Game");
                // don't switch player, so that the player can't make any more moves
                return;
            }

            updatePiecesVisual(currentGame.getBoard());
            currentGame.switchPlayer();
        }

    }

    @Override
    public void onMoveMade(Move move) {
        currentGame.handleMove(move);

        // if the move results in a capture, update the right panel
        updateCapturedPieces();

        // update history in left panel
        updateHistoryArea();
    }

    @Override
    public void onPieceSelected(Point point) {
        resetColor();

        Position pos;
        // get right coords
        if(isWhiteView) pos = new Position((char)('A' + point.x), 8 - point.y);
        else pos = new Position((char)('H' - point.x), point.y + 1);

        Piece ps = currentGame.getBoard().getPieceAt(pos);
        if (ps == null) return;

        if(ps.getColor() != currentGame.currentPlayerColor) return;

        for(Position p : ps.getPossibleMoves(currentGame.getBoard())){
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
        // for testing :
        // System.out.println( currentGame.getBoard().toString(Colors.WHITE));
    }

    public void updateCapturedPieces(){
        // update captured pieces based on each player's captured pieces

        Colors playerCol = currentGame.getPlayer().pieceColor;

        StringBuilder str = new StringBuilder();
        if(playerCol == Colors.WHITE){
            for(Piece ps : currentGame.getPlayer().getCapturedPieces()) {
                String s = getStrFromPiece(ps);
                str.append(s);
            }
            capturedPiecesWhite.setText(str.toString());

            str = new StringBuilder();
            for(Piece ps : currentGame.getOpponent().getCapturedPieces()) {
                String s = getStrFromPiece(ps);
                str.append(s);
            }
            capturedPiecesBlack.setText(str.toString());
        } else {
            for(Piece ps : currentGame.getPlayer().getCapturedPieces()) {
                String s = getStrFromPiece(ps);
                str.append(s);
            }
            capturedPiecesBlack.setText(str.toString());

            str = new StringBuilder();
            for(Piece ps : currentGame.getOpponent().getCapturedPieces()) {
                String s = getStrFromPiece(ps);
                str.append(s);
            }
            capturedPiecesWhite.setText(str.toString());
        }
    }

    public void updateHistoryArea(){
        StringBuilder str = new StringBuilder();
        for(Move mv : currentGame.getMoveList()){
            str.append(mv.toString()).append("\n");
        }
        historyArea.setText(str.toString());
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