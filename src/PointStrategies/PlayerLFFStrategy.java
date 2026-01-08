package PointStrategies;

import UIPanels.MainFrame;

public class PlayerLFFStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Game has ended from player forfeit");
        MainFrame.GamePanel.endOfGameLabelState.setText("<html>You lost from ff!<br/>-150 points</html>");
        return -150;
    }
}
