package game.controller;

import game.model.Direction;
import game.model.GameSquare;
import game.model.GameState;
import game.model.IGameModel;
import game.model.Player;
import game.view.GameView;

public interface IController {

    GameState getGameState();

    Player getCurPlayer();

    void autoSearch();

    boolean isHasComputer();

    boolean isValidMove(int c, int r);

    void move(int c, int r, Direction moveDirection, Player curPlayer);

    boolean isOver();

    void reStart();

    boolean canMoveAt(int c, int r);

    int getMilitary(Player player1);

    GameSquare[] getGameSquares();

    void changesOppositePlayer();

    void processGameState();

    void setModel(IGameModel model);

    void setCurView(GameView view);

    void runPlayersOptionalConfigurationGame();

}
