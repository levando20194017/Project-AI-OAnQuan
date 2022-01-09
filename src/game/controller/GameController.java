package game.controller;

import java.util.Iterator;

import javax.swing.JOptionPane;

import game.lib.AI.ComputerDecisionResult;
import game.model.Direction;
import game.model.GameSquare;
import game.model.GameState;
import game.model.IGameModel;
import game.model.Player;
import game.view.GameView;
import game.view.IView;

public class GameController implements IController {
    private IView view;
    private IGameModel gameModel;
    private Player curPlayer;

    public GameController() {

    }

    public GameController(IView v, IGameModel m) {
        this.view = v;
        this.gameModel = m;
    }

    @Override
    public GameState getGameState() {
        return gameModel.getGameState();
    }

    @Override
    public Player getCurPlayer() {
        return curPlayer;
    }

    @Override
    public void autoSearch() {
        ComputerDecisionResult rs = gameModel.autoSearch();
        move(rs.squareIndex, rs.dir, Player.PLAYER_2.isComputer() ? Player.PLAYER_2 : Player.PLAYER_1);
        System.out.println("Computer do move:" + rs.squareIndex + " " + rs.dir);
    }

    @Override
    public boolean isHasComputer() {
        return curPlayer.isComputer();
    }

    @Override
    public boolean isValidMove(int c, int r) {
        return gameModel.isValidMove(c, r, curPlayer);
    }

    @Override
    public void move(int c, int r, Direction moveDirection, Player curPlayer) {
        if (r < 0 || r > 1 || c < 0 || c > 5)
            return;
        int firstIndex = c + (r * 5 + (r > 0 ? 1 : 0));
        System.out.println(firstIndex + " " + curPlayer.getName());
        move(firstIndex, moveDirection, curPlayer);
    }

    private void move(int index, Direction moveDirection, Player curPlayer) {
        final int BOSS_2 = 6, BOSS_1 = 0;
        if (index == BOSS_1 || index == BOSS_2)
            return;
        gameModel.setLoopDirection(moveDirection);
        gameModel.setIndexLoop(index);

        int mitalries = gameModel.getAndRemoveMilitaryAt(index);
        if (mitalries == 0)
            return;
        Iterator<GameSquare> squares = gameModel.iterator();
        while (mitalries-- > 0) {
            GameSquare curSquare = squares.next();
            curSquare.setMilitaries(curSquare.getMilitaries() + 1);
            view.updateView(true);

        }
        GameSquare lastestLoopedSquare = squares.next();
        // System.out.println("On gameModelApdater In Move : lastestLooped : " +
        // lastestLooped.getIndex() + " direction "
        // + gameModel.getLastestLoopedDirection().name());

        if (lastestLoopedSquare.getMilitaries() == 0
                && lastestLoopedSquare.getSquareIndex() != BOSS_1
                && lastestLoopedSquare.getSquareIndex() != BOSS_2) {
            GameSquare nextLoop = squares.next();

            int nextMil = gameModel.getMilitaryAt(nextLoop.getSquareIndex());
            if (nextMil > 0) {
                if (nextLoop.isBossSquare()) {
                    nextLoop.setBossSquare(false);
                }
                curPlayer.militaries += nextMil;
                gameModel.removeMiltaryAt(nextLoop.getSquareIndex());
                System.out.println("mutil getting reward at " + nextLoop.getSquareIndex());
                nextLoop = squares.next();
                while (nextLoop.getMilitaries() == 0) {
                    nextLoop = squares.next();
                    int n = gameModel.getMilitaryAt(nextLoop.getSquareIndex());
                    if (n == 0) {
                        break;
                    }
                    if (nextLoop.isBossSquare())
                        nextLoop.setBossSquare(false);
                    gameModel.removeMiltaryAt(nextLoop.getSquareIndex());
                    curPlayer.militaries += n;
                    view.updateView(true);
                    nextLoop = squares.next();
                }
            }
        }
        if (lastestLoopedSquare.getMilitaries() != 0) {
            move(lastestLoopedSquare.getSquareIndex(), gameModel.getLastestLoopedDirection(), curPlayer);
        }
    }
}
