package PointStrategies;

import UIPanels.MainFrame;
import Utils.Game;

public class PlayerLFFStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Jocul s-a terminat prin ff din partea jucatorului");
        MainFrame.GamePanel.endOfGameLabelState.setText("Ai pierdut prin ff, -150 de puncte");
        return -150;
    }
}
