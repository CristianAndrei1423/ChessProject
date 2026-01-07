package UIPanels;

import Pieces.Piece;
import Utils.Move;
import Utils.Player;

import java.awt.*;

public interface GameObserver{
    void onPlayerSwitch();
    void onMoveMade(Move move);
    void onPieceCaptured(Piece piece);
    void onPieceSelected(Point point);
}
