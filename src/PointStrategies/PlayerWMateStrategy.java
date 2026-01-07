package PointStrategies;

import UIPanels.MainFrame;

public class PlayerWMateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Player has won through mate");
        MainFrame.GamePanel.endOfGameLabelState.setText("Player has won through mate, + 300 points");
        return 300;
    }
}
