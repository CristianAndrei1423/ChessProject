package PointStrategies;

import UIPanels.MainFrame;

public class PlayerWFFStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Player has won trough forfeit");
        MainFrame.GamePanel.endOfGameLabelState.setText("<html>Player has won through forfeit!<br/>+150 points</html>");
        return 150;
    }
}
