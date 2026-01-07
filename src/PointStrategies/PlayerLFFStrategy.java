package PointStrategies;

import UIPanels.MainFrame;

public class PlayerLFFStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Game has ended from player forfeit");
        MainFrame.GamePanel.endOfGameLabelState.setText("You lost from ff, -150 points");
        return -150;
    }
}
