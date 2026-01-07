package PointStrategies;

import UIPanels.MainFrame;
import Utils.Game;

public class PlayerWMateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Playerul a castigat prin mat");
        MainFrame.GamePanel.endOfGameLabelState.setText("Playerul a castigat prin mat, + 300");
        return 300;
    }
}
