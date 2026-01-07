package Utils;

import java.util.List;

public interface ChessPiece {
    boolean checkForCheck(Board board, Position kingPosition);
    char type();
}
