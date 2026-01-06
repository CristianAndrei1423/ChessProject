package UIPanels;

import Utils.Game;
import Utils.User;

import java.util.List;

public interface MenuObserver{
    void onMenuEnter(User user);
    void onContinueOption(List<Game> gameList);
    void onNewGame();
}
