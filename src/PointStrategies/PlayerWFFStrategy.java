package PointStrategies;

import UIPanels.MainFrame;

public class PlayerWFFStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Player has won trough forfeit");
        MainFrame.GamePanel.endOfGameLabelState.setText("Player has won through forfeit, + 150 points");
        return 150;
    }
}
