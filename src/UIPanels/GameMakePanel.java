package UIPanels;

import Utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GameMakePanel extends JPanel implements GameMakeObserver {
    private final JRadioButton white;
    private final JTextArea alias;

    public Game gameMade;

    public GameMakePanel() {
        setLayout(new GridBagLayout());
        setBackground(PanelColors.DARKER_BLUE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(PanelColors.DARKER_BLUE);

        alias = new JTextArea();

        JPanel colors = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        white = new JRadioButton("White");
        JRadioButton black = new JRadioButton("Black");

        ButtonGroup group = new ButtonGroup();
        group.add(white);
        group.add(black);

        // by default white
        white.setSelected(true);
        colors.add(white);
        colors.add(black);

        JButton startGame = new JButton("Start game");
        startGame.setBackground(Color.GREEN);

        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.showCard("GAME");
            }
        });

        content.add(new JLabel("Provide an alias for you"));
        content.add(alias);
        content.add(new JLabel("What color would you like to start as: "));
        content.add(colors);
        content.add(Box.createVerticalStrut(10));
        content.add(startGame);

        add(content);
    }

    @Override
    public void onGameMade() {
        Colors color = (white.isSelected() ? Colors.WHITE : Colors.BLACK);

        Player player = new Player(Main.getChessGame().currentUser.getEmail(), color);

        Colors opColor = (color == Colors.BLACK ? Colors.WHITE : Colors.BLACK);

        Player opp = new Player("computer", opColor);

        // make game

        // here board is initialized too
        Game game = new Game(Main.getChessGame().lastGameId);
        Main.getChessGame().currentUser.addGID(Main.getChessGame().lastGameId);

        Main.getChessGame().lastGameId++;

        List<Player> players = new ArrayList<>();
        players.add(player);
        players.add(opp);
        game.setPlayers(players);

        Main.getChessGame().currentUser.addGame(game);

        Main.getChessGame().gameMap.put(Main.getChessGame().lastGameId-1, game);

        gameMade = game;

        gameMade.playerAlias = alias.getText();
    }

}