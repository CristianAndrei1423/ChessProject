package PointStrategies;

import UIPanels.MainFrame;

public class PlayerLMateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Player has lost through mate");
        MainFrame.GamePanel.endOfGameLabelState.setText("<html>Player has lost through mate!<br/>-300 points</html>");
        return -300;
    }
}
