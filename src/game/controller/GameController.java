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

    // kiểm tra người chơi hiện tại có phải là máy ko
    @Override
    public boolean isHasComputer() {
        return curPlayer.isComputer();
    }

    // nước đi hợp lệ
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
        // nếu mà index chỉ ở ô boss thì ko thể di chuyển
        if (index == BOSS_1 || index == BOSS_2)
            return;
        gameModel.setLoopDirection(moveDirection);// thiết lập hướng di chuyển
        gameModel.setFirstIndexOfLoop(index);//

        int mitalries = gameModel.getAndRemoveMilitaryAt(index); // bốc quân lên
        if (mitalries == 0)
            return;
        Iterator<GameSquare> squares = gameModel.iterator();
        while (mitalries-- > 0) {
            GameSquare curSquare = squares.next();
            curSquare.setMilitaries(curSquare.getMilitaries() + 1); // cộng thêm 1 quân khi đi qua
            view.updateView(true); // giao diện

        }
        GameSquare lastestLoopedSquare = squares.next();
        // System.out.println("On gameModelApdater In Move : lastestLooped : " +
        // lastestLooped.getIndex() + " direction "
        // + gameModel.getLastestLoopedDirection().name());

        if (lastestLoopedSquare.getMilitaries() == 0 // nếu ô cuối cùng ko có quân nào
                && lastestLoopedSquare.getSquareIndex() != BOSS_1
                && lastestLoopedSquare.getSquareIndex() != BOSS_2) {
            GameSquare nextLoop = squares.next(); // trỏ sang ô kế tiếp và kiểm tra

            int nextMil = gameModel.getMilitaryAt(nextLoop.getSquareIndex()); // lấy số quân ở ô kế tiếp gắn cho nextMil
            if (nextMil > 0) {
                if (nextLoop.isBossSquare()) { // kiểm tra nếu là ô boss
                    nextLoop.setBossSquare(false);
                }
                curPlayer.militaries += nextMil; // cộng điểm cho người chơi lần ăn ô thứ nhất
                gameModel.removeMiltaryAt(nextLoop.getSquareIndex()); // sau khi bốc số quân lên thì mình remove cái ô
                                                                      // đấy (số quân lúc này = 0)
                System.out.println("mutil getting reward at " + nextLoop.getSquareIndex());
                nextLoop = squares.next(); // sang ô kế tiếp để kiểm tra các ô sau có được tính điểm ko
                while (nextLoop.getMilitaries() == 0) {
                    nextLoop = squares.next();
                    int n = gameModel.getMilitaryAt(nextLoop.getSquareIndex());
                    if (n == 0) {
                        break;
                    }
                    if (nextLoop.isBossSquare())
                        nextLoop.setBossSquare(false);
                    gameModel.removeMiltaryAt(nextLoop.getSquareIndex());
                    curPlayer.militaries += n; // cộng điểm cho người chơi hiện tại và remove ô vuông đấy
                    // sau đó kiểm tra sang các ô tiếp theo, người chơi được tính điểm nếu sole với
                    // ô vuông ko có quân
                    view.updateView(true);
                    nextLoop = squares.next();
                }
            }
        }
        if (lastestLoopedSquare.getMilitaries() != 0) { // nếu ô cuoosu != 0 và ko phải ô boss thì di chuyển tiếp
            move(lastestLoopedSquare.getSquareIndex(), gameModel.getLatestLoopDirection(), curPlayer);
        }
    }

    @Override
    public boolean isOver() { // kết thúc
        return gameModel.isEndGame();
    }

    @Override
    public void reStart() { // game mới
        gameModel.reAssign();
    }

    @Override
    public boolean canMoveAt(int c, int r) {
        int index = c + (r * 5 + (r > 0 ? 1 : 0));
        return gameModel.getMilitaryAt(index) > 0;
    }

    @Override
    public int getMilitary(Player p) {
        return p.militaries;
    }

    @Override
    public GameSquare[] getGameSquares() {
        return gameModel.getGameSquares();
    }

    @Override
    public void changesOppositePlayer() {
        processGameState();
        if (!checkRentingMitaries()) {
            view.toMessage(getOppositePlayer().getName() + " Won!");
            view.updateView(false);
            showReplay();
        }
        curPlayer = getOppositePlayer();
        if (!checkRentingMitaries()) {
            view.toMessage(getOppositePlayer().getName() + " Won!");
            view.updateView(false);
            showReplay();
        }
    }

    private boolean checkRentingMitaries() { // kiểm tra nếu số quân ở các ô bên phía người chơi đã hết thì ...
        if (!gameModel.stillHaveMilitaryOnBoard(curPlayer)) {
            if (curPlayer.militaries < 5) {
                gameModel.outMilitariesIf(curPlayer);
            } else {

                System.out.println(curPlayer.getName() + " rentting");
                gameModel.outMilitaries(curPlayer);
            }
        }
        return true;
    }

    private Player getOppositePlayer() {
        // trả về người chơi hiện tại
        if (this.curPlayer == Player.PLAYER_1)
            return Player.PLAYER_2;
        else
            return Player.PLAYER_1;
    }

    @Override
    public void processGameState() {
        {
            GameState s = getGameState();
            switch (s) {
                case DRAW:
                    view.toMessage("DRAWING GAME!");
                    break;
                case PLAYER1_WIN:
                    view.toMessage(Player.PLAYER_1.getName() + " Won!");
                    break;
                case PLAYER2_WIN:
                    view.toMessage(Player.PLAYER_2.getName() + " Won!");
                    break;

                default:
                    return;
            }
            showReplay();
        }
    }

    private void showReplay() {
        String[] options = new String[] { "Yes", "No" };
        int response = JOptionPane.showOptionDialog(null, "Do you want to replay?", "", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (response == 0) {
            reStart();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void setModel(IGameModel model) {
        this.gameModel = model;
    }

    @Override
    public void setCurView(GameView view) {
        this.view = view;
    }

    @Override
    public void runPlayersOptionalConfigurationGame() {
        String[] options = new String[] { "Yes", "No" };

        final int NO_OPTION = 1;
        int wannaPlayFirst = JOptionPane.showOptionDialog(null, "Do you want to be first player?", "",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        // the person is default player 1
        this.curPlayer = Player.PLAYER_1;

        int wannaHaveComputer = 1;

        if (wannaHaveComputer == 1) {
            getOppositePlayer().setComputer(true);
            System.out.println("oppositePlayer " + getOppositePlayer().getName());
            gameModel.setGameLevel(getInputGameLevel());
            curPlayer.setName("You");
            getOppositePlayer().setName("Computer");
            if (wannaPlayFirst == NO_OPTION) {
                Thread s = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        autoSearch();
                        view.updateView(false);
                    }
                });
                s.start();
            }
        }

    }

    private int getInputGameLevel() {
        String[] options = new String[] { "Easy", "Medium", "Hard", "Super Hard" };
        final int EASY_OP = 0, MEDIUM_OP = 1, HARD_OP = 2, SU_HAR_OP = 3;

        int inputLevel = JOptionPane.showOptionDialog(null, "Please choice your enemy!", "", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        int level = -1;
        if (inputLevel == EASY_OP)
            level = 1;
        if (inputLevel == MEDIUM_OP)
            level = 3;
        if (inputLevel == HARD_OP)
            level = 5;
        if (inputLevel == SU_HAR_OP)
            level = 7;
        return level;
    }
}
