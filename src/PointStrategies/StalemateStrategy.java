package PointStrategies;

import UIPanels.MainFrame;

public class StalemateStrategy implements PointsStrategy{
    @Override
    public int pointsDeducted() {
        System.out.println("Game has ended in a stalemate");
        MainFrame.GamePanel.endOfGameLabelState.setText("<html>Game has ended in a stalemate!<br/>+150 points</html>");
        return 150;
    }
}
