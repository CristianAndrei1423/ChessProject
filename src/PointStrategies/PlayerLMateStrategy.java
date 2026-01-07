package PointStrategies;

import UIPanels.MainFrame;

public class PlayerLMateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Player has lost through mate");
        MainFrame.GamePanel.endOfGameLabelState.setText("Player has lost through mate, -300 points");
        return -300;
    }
}
