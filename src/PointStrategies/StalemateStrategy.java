package PointStrategies;

import UIPanels.MainFrame;
import Utils.Game;

public class StalemateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Jocul s-a terminat prin remiza");
        MainFrame.GamePanel.endOfGameLabelState.setText("Jocul s-a terminat prin remiza, +150 puncte");
        return 150;
    }
}
