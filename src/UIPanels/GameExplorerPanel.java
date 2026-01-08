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

    private final JLabel[][] squares;
    private final JPanel gamesButtonPanel;
    private final JLabel topLabel;    // For Computer/Opponent
    private final JLabel bottomLabel; // For Player/User
    public Game gameToBeContinued;
    private final JButton playButton;

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
        explorerPanel.setBackground(PanelColors.THEME_DARK_BLUE);
        explorerPanel.setPreferredSize(new Dimension(250, 0));
        explorerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel availableLabel = new JLabel("Your Active Games");
        availableLabel.setForeground(Color.WHITE);
        availableLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        availableLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        explorerPanel.add(availableLabel, BorderLayout.NORTH);

        gamesButtonPanel = new JPanel();
        gamesButtonPanel.setLayout(new BoxLayout(gamesButtonPanel, BoxLayout.Y_AXIS));
        gamesButtonPanel.setBackground(PanelColors.THEME_DARK_BLUE);

        JScrollPane scrollPane = new JScrollPane(gamesButtonPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(PanelColors.THEME_DARK_BLUE);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        explorerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonsPanel.setBackground(PanelColors.THEME_DARK_BLUE);
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

        playButton = new JButton("Continue Game");
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

        // set invisible until user selects a game
        playButton.setVisible(false);

        buttonsPanel.add(playButton);
        buttonsPanel.add(btnBack);

        explorerPanel.add(buttonsPanel, BorderLayout.SOUTH);


        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(PanelColors.DARKER_BLUE);
        // -------------------------------------------------------------

        // top label (Opponent)
        topLabel = new JLabel("Opponent", SwingConstants.CENTER);
        topLabel.setForeground(Color.LIGHT_GRAY);
        topLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topLabel.setBorder(new EmptyBorder(15, 0, 10, 0));
        rightPanel.add(topLabel, BorderLayout.NORTH);

        // board
        JPanel boardWrapper = new JPanel(new GridBagLayout());
        boardWrapper.setBackground(PanelColors.DARKER_BLUE);

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

        // bottom label (Player)
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
                gamesButtonPanel.add(Box.createVerticalStrut(10));
            }
        }

        gamesButtonPanel.revalidate();
        gamesButtonPanel.repaint();
    }

    private JButton createGameButton(Game game, User currentUser) {
        String myEmail = currentUser.getEmail();
        String p1Name = game.getPlayer().name;
        String p2Name = game.getOpponent().name;

        String opponentName = p1Name.equals(myEmail) ? p2Name : p1Name;

        String text = "Game " + game.gameId + " vs " + opponentName ;

        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 60)); // Fixed width, taller for 2 lines
        btn.setPreferredSize(new Dimension(220, 60));
        btn.setBackground(new Color(44, 54, 73));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButton.setVisible(true);
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

        String pName = game.getPlayer().name;

        if (pName.equals(currentEmail)) {
            me = game.getPlayer();
            opponent = game.getOpponent();
        } else {
            me = game.getOpponent();
            opponent = game.getPlayer();
        }

        bottomLabel.setText(game.playerAlias + " (" + currentEmail + ")");
        topLabel.setText(opponent.name + " (computer)");

        boolean isWhiteView = (me.pieceColor == Colors.WHITE);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // map buttons to actual chess board positions
                Position pos;

                if (isWhiteView) pos = new Position((char) ('A' + col), 8 - row);
                else pos = new Position((char) ('H' - col), row + 1);

                Piece p = game.getBoard().getPieceAt(pos);
                squares[row][col].setIcon(getIconFromPiece(p));
            }
        }
    }

    private void resetBoardVisuals(){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setIcon(empty);
            }
        }
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