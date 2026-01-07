package PointStrategies;

import UIPanels.MainFrame;
import Utils.Game;

public class PlayerWFFStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Playerul a castigat prin ff");
        MainFrame.GamePanel.endOfGameLabelState.setText("Playerul a castigat prin ff, + 150 puncte");
        return 150;
    }
}
