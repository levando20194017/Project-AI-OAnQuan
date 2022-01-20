package game.launcher;

import javax.swing.JFrame;

import game.controller.GameController;
import game.controller.IController;
import game.loader.ImageLoader;
import game.model.GameBoard;
import game.model.IGameModel;
import game.view.GameView;

public class Launcher {
    public static void main(String[] args) {
        JFrame f = new JFrame("O An Quan");
        IGameModel model = new GameBoard();
        IController controller = new GameController();
        GameView view = new GameView(new ImageLoader(), controller);
        controller.setModel(model);
        controller.setCurView(view);

        controller.runPlayersOptionalConfigurationGame();
        f.add(view);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
