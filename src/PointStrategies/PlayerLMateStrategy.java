package PointStrategies;

import Utils.Game;
import UIPanels.MainFrame;

public class PlayerLMateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Playerul a pierdut prin mat");
        MainFrame.GamePanel.endOfGameLabelState.setText("Playerul a pierdut prin mat, -300 de puncte");
        return -300;
    }
}
