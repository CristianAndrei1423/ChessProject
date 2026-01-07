package UIPanels;

import Utils.*;

import javax.swing.*;
import java.awt.*;
import java.lang.classfile.attribute.LineNumberInfo;


public class MainFrame extends JFrame {
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    public static GamePanel GamePanel;
    private JPanel LoginPanel;
    private static MainMenuPanel MainMenuPanel;
    private JPanel SignUpPanel;
    private static GameMakePanel GameMakePanel;
    public static MainFrame gameFrame;

    public static void main(String[] args){
        gameFrame = new MainFrame();
    }

    public MainFrame() {
        setTitle("ChessMaster");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPanel = new LoginPanel(this);
        MainMenuPanel = new MainMenuPanel(this);
        GamePanel = new GamePanel(null);
        SignUpPanel = new SignUpPanel(this);
        GameMakePanel = new GameMakePanel(this);

        // add the screens
        mainPanel.add(LoginPanel, "LOGIN");
        mainPanel.add(MainMenuPanel, "MENU");
        mainPanel.add(GamePanel, "GAME");
        mainPanel.add(SignUpPanel, "SIGNUP");
        mainPanel.add(GameMakePanel, "NEWGAME");

        add(mainPanel);
        setVisible(true);
    }

    public static void showCard(String cardName) {
        if(cardName.equals("MENU"))
            MainMenuPanel.onMenuEnter(Main.getChessGame().currentUser);

        if(cardName.equals("GAME")){
            // initialize game within panel then use game
            GameMakePanel.onGameMade();
            Game game = GameMakePanel.gameMade;
            GamePanel.endOfGameLabelState.setVisible(false);

            // here I need to start the game
            // equivalent to start

            // if it's a new game, initialize everything
            if(game.currentPlayerInd == -1) {
                game.initalizeBoard();
                game.gameStillValid = true;
                game.initalizeOwnedPieces();
                game.currentPlayerInd = game.getPlayer().pieceColor == Colors.WHITE ? 1 : 2;
                // game.currentPlayerColor = Colors.WHITE;
                GamePanel.currentGame = game;
                GamePanel.isWhiteView = GamePanel.currentGame.getPlayer().pieceColor == Colors.WHITE;
                GamePanel.updatePiecesVisual(game.getBoard());
                System.out.println("Game started !");

                // if the first to move is the computer
                if(game.currentPlayerColor != game.getPlayer().pieceColor){
                    game.runForComputer();
                    GamePanel.updatePiecesVisual(game.getBoard());
                    game.switchPlayer();
                }

                // System.out.println(GamePanel.currentGame.getBoard().toString());
            }

            // then I need to run the program and update on each input
            // this is handled in GamePanel
        }

        cardLayout.show(mainPanel, cardName);
    }

}