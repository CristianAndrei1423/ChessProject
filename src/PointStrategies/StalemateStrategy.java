package PointStrategies;

import UIPanels.MainFrame;

public class StalemateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Game has ended in a stalemate");
        MainFrame.GamePanel.endOfGameLabelState.setText("Game has ended in a stalemate, +150 points");
        return 150;
    }
}
