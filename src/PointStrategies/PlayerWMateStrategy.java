package PointStrategies;

import UIPanels.MainFrame;

public class PlayerWMateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Player has won through mate");
        MainFrame.GamePanel.endOfGameLabelState.setText("<html>Player has won through mate!<br/>+300 points</html>");
        return 300;
    }
}
