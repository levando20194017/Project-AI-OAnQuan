package game.controller;

import game.model.Direction;
import game.model.GameSquare;
import game.model.GameState;
import game.model.IGameModel;
import game.model.Player;
import game.view.GameView;

public interface IController {

    GameSquare[] getGameSquares();

    GameState getGameState();

    Player getCurPlayer();

    void autoSearch();

    boolean isComputer();

    boolean isValidMove(int c, int r);

    void move(int c, int r, Direction moveDirection, Player curPlayer);

    boolean isOver();

    void startOver();

    boolean canMoveAt(int c, int r);

    int getMilitaries(Player player1);

    void changesOppositePlayer();

    void processGameState();

    void setModel(IGameModel model);

    void setCurView(GameView view);

    void runPlayersOptionalConfigurationGame();

}
