package UIPanels;

import Utils.Move;

import java.awt.*;

public interface GameObserver{
    void onPlayerSwitch();
    void onMoveMade(Move move);
    void onPieceSelected(Point point);
}
