package game.lib.AI;

import game.model.GameBoard;
import game.model.Player;

public interface IAutoSearching {
    ComputerDecisionResult doSearch(GameBoard gb, Player Player1, Player player2, int maxDepth);
}
