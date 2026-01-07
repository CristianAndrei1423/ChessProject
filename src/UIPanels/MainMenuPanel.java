package UIPanels;

import Utils.Main;
import Utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainMenuPanel extends JPanel {
    JLabel TotPoints;
    JLabel ActiveGames;
    public MainFrame frame;
    public MainMenuPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        this.frame = frame;
        setBackground(new Color(20, 25, 40));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(20, 25, 40));

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        statsPanel.setBackground(new Color(20, 25, 40));

        TotPoints = new JLabel("Total points :");
        ActiveGames = new JLabel("Active Games :");

        statsPanel.add(TotPoints);
        statsPanel.add(ActiveGames);

        JButton btnNew = createMenuButton("New Game", "Start match vs Computer",new Color(34, 197, 94));
        JButton btnContinue = createMenuButton("Continue Game", "Resume a game in progress", new Color(0, 59, 255));
        JButton btnLogout = createMenuButton("Logout", "Return to login", new Color(255, 0, 0));

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // when user logs out
                Main.getChessGame().currentUser = null;
                MainFrame.showCard("LOGIN");
                Main.getChessGame().write();
            }
        });
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.showCard("NEWGAME");
            }
        });
        btnContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.showCard("GAMEEXPLORER");
            }
        });

        content.add(statsPanel);
        content.add(Box.createVerticalStrut(30));
        content.add(btnNew);
        content.add(Box.createVerticalStrut(10));
        content.add(btnContinue);
        content.add(Box.createVerticalStrut(10));
        content.add(btnLogout);

        add(content);
    }

    private JButton createMenuButton(String title, String subtitle, Color accent) {
        JButton btn = new JButton("<html><center><b style='font-size:12px'>" + title + "</b><br><span style='font-size:9px'>" + subtitle + "</span></center></html>");
        btn.setBackground(new Color(30, 40, 60));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, accent)); // The colored strip on left
        btn.setMaximumSize(new Dimension(300, 60));
        return btn;
    }

    public void onMenuEnter(User user) {
        TotPoints.setText("Total points : " + user.getPoints());
        ActiveGames.setText("Active Games : " + user.getActiveGames().size());
    }
}
