package UIPanels;

import Utils.*;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    public static GamePanel GamePanel;
    private static MainMenuPanel MainMenuPanel;
    private static GameMakePanel GameMakePanel;
    public static MainFrame gameFrame;
    public static GameExplorerPanel GameExplorerPanel;

    public static void main(){
        gameFrame = new MainFrame();
    }

    public MainFrame() {
        setTitle("ChessMaster");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel loginPanel = new LoginPanel(this);
        MainMenuPanel = new MainMenuPanel(this);
        GamePanel = new GamePanel(null);
        JPanel signUpPanel = new SignUpPanel(this);
        GameMakePanel = new GameMakePanel();
        GameExplorerPanel = new GameExplorerPanel();

        // add the screens
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(MainMenuPanel, "MENU");
        mainPanel.add(GamePanel, "GAME");
        mainPanel.add(signUpPanel, "SIGNUP");
        mainPanel.add(GameMakePanel, "NEWGAME");
        mainPanel.add(GameExplorerPanel, "GAMEEXPLORER");

        add(mainPanel);
        setVisible(true);
    }

    public static void showCard(String cardName) {
        if(cardName.equals("MENU"))
            MainMenuPanel.onMenuEnter(Main.getChessGame().currentUser);

        if(cardName.equals("GAME")){

            if(GameExplorerPanel.gameToBeContinued != null){
                // if the game is continued :
                Game game = GameExplorerPanel.gameToBeContinued;
                game.currentPlayerInd = game.getPlayer().pieceColor == Colors.WHITE ? 1 : 2;
                GamePanel.endOfGameLabelState.setVisible(false);
                GamePanel.currentGame = game;
                GamePanel.isWhiteView = GamePanel.currentGame.getPlayer().pieceColor == Colors.WHITE;
                GamePanel.updatePiecesVisual(game.getBoard());
                System.out.println("Game continued !");
                GameExplorerPanel.gameToBeContinued = null;
                game.addObserver(GamePanel);
                GamePanel.updateHistoryArea();
                GamePanel.updateCapturedPieces();
            }
            else {
                // initialize game within panel then use game
                GameMakePanel.onGameMade();
                Game game = GameMakePanel.gameMade;
                game.addObserver(GamePanel);
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
                    GamePanel.updateHistoryArea();
                    GamePanel.updateCapturedPieces();

                    // if the first to move is the computer
                    if(game.currentPlayerColor != game.getPlayer().pieceColor){
                        game.runForComputer();
                        GamePanel.updatePiecesVisual(game.getBoard());
                        game.switchPlayer();
                    }
                }
            }
        }

        if(cardName.equals("GAMEEXPLORER")) GameExplorerPanel.refreshGameList();

        cardLayout.show(mainPanel, cardName);
    }

}