package MoveStrategies;

import Utils.Board;
import Utils.Position;

import java.util.List;

public interface MoveStrategy {
    List<Position> getPossibleMoves(Board board, Position from);
}
