package UIPanels;

import Pieces.Piece;
import Utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameExplorerPanel extends JPanel {

    private JLabel[][] squares;
    private JPanel gamesButtonPanel;
    private JLabel topLabel;    // For Computer/Opponent
    private JLabel bottomLabel; // For Player/User
    public Game gameToBeContinued;

    static ImageIcon[] whiteIcons;
    static ImageIcon[] blackIcons;
    static ImageIcon empty;

    public GameExplorerPanel() {
        // Initialize images
        initIcons();

        gameToBeContinued = null;

        // Main Layout
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0));

        // left side -> Explorer panel --------------------------------------
        JPanel explorerPanel = new JPanel(new BorderLayout());
        explorerPanel.setBackground(new Color(30, 40, 60));
        explorerPanel.setPreferredSize(new Dimension(250, 0));
        explorerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel availableLabel = new JLabel("Your Active Games");
        availableLabel.setForeground(Color.WHITE);
        availableLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        availableLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        explorerPanel.add(availableLabel, BorderLayout.NORTH);

        gamesButtonPanel = new JPanel();
        gamesButtonPanel.setLayout(new BoxLayout(gamesButtonPanel, BoxLayout.Y_AXIS));
        gamesButtonPanel.setBackground(new Color(30, 40, 60));

        JScrollPane scrollPane = new JScrollPane(gamesButtonPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 40, 60));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        explorerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonsPanel.setBackground(new Color(30, 40, 60));
        buttonsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton btnBack = new JButton("Back to Menu");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(200, 40));
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoardVisuals();
                gameToBeContinued = null;
                MainFrame.showCard("MENU");
            }
        });

        JButton playButton = new JButton("Continue Game");
        playButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        playButton.setBackground(Color.GRAY);
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setPreferredSize(new Dimension(200, 40));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoardVisuals();
                MainFrame.showCard("GAME");
            }
        });

        buttonsPanel.add(playButton);
        buttonsPanel.add(btnBack);

        explorerPanel.add(buttonsPanel, BorderLayout.SOUTH);


        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(20, 25, 40));
        // -------------------------------------------------------------

        // -- Top Label (Opponent) --
        topLabel = new JLabel("Opponent", SwingConstants.CENTER);
        topLabel.setForeground(Color.LIGHT_GRAY);
        topLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topLabel.setBorder(new EmptyBorder(15, 0, 10, 0));
        rightPanel.add(topLabel, BorderLayout.NORTH);

        // -- The Board --
        JPanel boardWrapper = new JPanel(new GridBagLayout());
        boardWrapper.setBackground(new Color(20, 25, 40));

        JPanel board = new JPanel(new GridLayout(8, 8));
        board.setPreferredSize(new Dimension(500, 500));

        squares = new JLabel[8][8];
        boolean white = true;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JLabel square = new JLabel();
                squares[row][col] = square;
                square.setOpaque(true); // Required for background color on JLabel

                if (white) square.setBackground(new Color(240, 217, 181));
                else square.setBackground(new Color(181, 136, 99));

                square.setHorizontalAlignment(SwingConstants.CENTER);
                square.setVerticalAlignment(SwingConstants.CENTER);

                board.add(square);
                white = !white;
            }
            white = !white;
        }
        boardWrapper.add(board);
        rightPanel.add(boardWrapper, BorderLayout.CENTER);

        // -- Bottom Label (Player) --
        bottomLabel = new JLabel("Player", SwingConstants.CENTER);
        bottomLabel.setForeground(Color.GREEN);
        bottomLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        bottomLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
        rightPanel.add(bottomLabel, BorderLayout.SOUTH);

        // ------------------------------------------------------------------------

        add(explorerPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    public void refreshGameList() {
        gamesButtonPanel.removeAll();

        User currentUser = Main.getChessGame().currentUser;
        if (currentUser != null) {
            List<Game> games = currentUser.getActiveGames();
            for (Game g : games) {
                JButton btn = createGameButton(g, currentUser);
                gamesButtonPanel.add(btn);
                gamesButtonPanel.add(Box.createVerticalStrut(10)); // Space between buttons
            }
        }

        gamesButtonPanel.revalidate();
        gamesButtonPanel.repaint();
    }

    private JButton createGameButton(Game game, User currentUser) {
        String myEmail = currentUser.getEmail();
        String p1Name = getPlayerName(game.getPlayer());
        String p2Name = getPlayerName(game.getOpponent());

        String opponentName = p1Name.equals(myEmail) ? p2Name : p1Name;

        String text = "<html><center>Game " + game.gameId + "<br/>vs " + opponentName + "</center></html>";

        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 60)); // Fixed width, taller for 2 lines
        btn.setPreferredSize(new Dimension(220, 60));
        btn.setBackground(new Color(45, 55, 75));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 70, 90), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBoardVisuals(game);
                gameToBeContinued = game;
            }
        });

        return btn;
    }

    private void updateBoardVisuals(Game game) {
        if (game == null) return;

        User currentUser = Main.getChessGame().currentUser;
        String currentEmail = (currentUser != null) ? currentUser.getEmail() : "";

        Player me;
        Player opponent;

        String pName = getPlayerName(game.getPlayer());

        if (pName.equals(currentEmail)) {
            me = game.getPlayer();
            opponent = game.getOpponent();
        } else {
            me = game.getOpponent();
            opponent = game.getPlayer();
        }

        // Update Labels
        bottomLabel.setText(game.playerAlias + " (" + currentEmail + ")");
        topLabel.setText(getPlayerName(opponent) + " (computer)");

        // Determine Orientation
        boolean isWhiteView = (me.pieceColor == Colors.WHITE);

        // Update Pieces on Board
        Board boardData = game.getBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Map visual grid (row,col) to backend Position
                Position pos;
                if (isWhiteView) {
                    // White View: Top-Left (0,0) is A8
                    pos = new Position((char) ('A' + col), 8 - row);
                } else {
                    // Black View: Top-Left (0,0) is H1
                    pos = new Position((char) ('H' - col), row + 1);
                }

                Piece p = boardData.getPieceAt(pos);
                squares[row][col].setIcon(getIconFromPiece(p));
            }
        }

        revalidate();
        repaint();
    }

    private void resetBoardVisuals(){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setIcon(empty);
            }
        }
    }

    private String getPlayerName(Player p) {
        if (p == null) return "Unknown";
        String s = p.toString();
        int start = "Player : ".length();
        int end = s.indexOf(" as color");
        if (end > start) {
            return s.substring(start, end);
        }
        return "Unknown";
    }

    private void initIcons() {
        whiteIcons = new ImageIcon[9];
        blackIcons = new ImageIcon[9];

        // Ensure these paths exist in your project
        whiteIcons[0] = new ImageIcon("src/ChessImages/WhiteRook.png");
        whiteIcons[1] = new ImageIcon("src/ChessImages/WhiteKnight.png");
        whiteIcons[2] = new ImageIcon("src/ChessImages/WhiteBishop.png");
        whiteIcons[3] = new ImageIcon("src/ChessImages/WhiteQueen.png");
        whiteIcons[4] = new ImageIcon("src/ChessImages/WhiteKing.png");
        whiteIcons[8] = new ImageIcon("src/ChessImages/WhitePawn.png");

        blackIcons[0] = new ImageIcon("src/ChessImages/BlackRook.png");
        blackIcons[1] = new ImageIcon("src/ChessImages/BlackKnight.png");
        blackIcons[2] = new ImageIcon("src/ChessImages/BlackBishop.png");
        blackIcons[3] = new ImageIcon("src/ChessImages/BlackQueen.png");
        blackIcons[4] = new ImageIcon("src/ChessImages/BlackKing.png");
        blackIcons[8] = new ImageIcon("src/ChessImages/BlackPawn.png");

        empty = new ImageIcon("src/ChessImages/Empty.png");
    }

    private ImageIcon getIconFromPiece(Piece piece) {
        if (piece == null) return empty;

        if (piece.getColor() == Colors.WHITE) {
            switch (piece.type()) {
                case 'N': return whiteIcons[1];
                case 'R': return whiteIcons[0];
                case 'B': return whiteIcons[2];
                case 'Q': return whiteIcons[3];
                case 'K': return whiteIcons[4];
                case 'P': return whiteIcons[8];
            }
        } else {
            switch (piece.type()) {
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