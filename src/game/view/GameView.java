package game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import game.controller.IController;
import game.loader.ImageLoader;
import game.model.Direction;
import game.model.GameSquare;
import game.model.Player;

public class GameView extends JPanel implements IView {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ImageLoader imgLoader;
    private Image[] defaultBackGround;
    private IController controller;
    int offSetX = 240;
    int offSetY = 236;
    boolean isShowingButtonDirection = false;

    public GameView(ImageLoader imgLoader, IController c) {
        this.imgLoader = imgLoader;
        this.controller = c;
        // thiết lập background
        initDefaultBackGround();

        // java component
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int r = getMappingRow(e.getY());
                    int c = getMappingCol(e.getX());
                    if (!isShowingButtonDirection && isInBound(c, r)) {
                        if (controller.canMoveAt(c, r))
                            drawDirectionButton(c, r);
                    } else if (isShowingButtonDirection && isInBound(c, r)) {
                        Direction moveDirection = null;
                        boolean isOn = false;
                        if (rightOfHalft(e.getX(), e.getY())) {
                            moveDirection = Direction.RIGHT;
                            isOn = true;
                        } else if (leftOfHalf(e.getX(), e.getY())) {
                            moveDirection = Direction.LEFT;
                            isOn = true;
                        }
                        if (isOn) {
                            isShowingButtonDirection = !isShowingButtonDirection;
                            if (controller.isValidMove(c, r)) {
                                controller.move(c, r, moveDirection, controller.getCurPlayer());
                                if (!controller.isOver()) {
                                    controller.changesOppositePlayer();
                                    toMessage("Next turn is: " + controller.getCurPlayer().getName());
                                    if (controller.isHasComputer()) {
                                        controller.autoSearch();
                                        controller.changesOppositePlayer();
                                        toMessage("Next turn is: " + controller.getCurPlayer().getName());
                                    }
                                } else {
                                    System.out.println("comming herere i don know why");
                                    repaint();
                                    controller.processGameState();
                                }
                            }

                            repaint();
                        }
                    } else {
                        isShowingButtonDirection = !isShowingButtonDirection;
                        repaint();
                    }
                }
            }
        });

    }

    protected boolean rightOfHalft(int x, int y) {
        int mappingX = getMappingCol(x);
        int mappingY = getMappingRow(y);
        System.out.println("On method RightOfHalf in GameView " + "mappringX: " + mappingX + " mappingY " + mappingY);
        int offX = (getPreferredSize().width / 2) - 3 * 64 + 32;
        int offY = getPreferredSize().height / 2 - 64;
        System.out.println("On method RightOfHalf in GameView " + " offX: " + offX + " offY: " + offY);
        Point buttonRe = new Point(offX + (mappingX - 1) * 64, offY + mappingY * 64);
        Point clickBound = new Point(x, y);

        if (clickBound.x >= buttonRe.x + 32 && clickBound.x <= buttonRe.x + 64 && clickBound.y >= buttonRe.y
                && clickBound.y < buttonRe.y + 64)
            return true;
        return false;

    }

    protected boolean leftOfHalf(int x, int y) {
        int mappingX = getMappingCol(x);
        int mappingY = getMappingRow(y);
        int offX = (getPreferredSize().width / 2) - 3 * 64 + 32;
        int offY = getPreferredSize().height / 2 - 64;
        Point buttonRe = new Point(offX + (mappingX - 1) * 64, offY + mappingY * 64);
        Point clickBound = new Point(x, y);

        if (clickBound.x >= buttonRe.x && clickBound.x <= buttonRe.x + 32 && clickBound.y >= buttonRe.y
                && clickBound.y < buttonRe.y + 64)
            return true;
        return false;
    }

    // không gian mà mình có thể click vào ô vuông theo chiều ngang
    // mỗi ô vuông có kích thước 64x64
    private int getMappingCol(int x) {
        int ret = -1;
        if (x > 240 && x <= 240 + 64)
            ret = 1;
        if (x > 304 && x <= 304 + 64)
            ret = 2;
        if (x > 368 && x <= 368 + 64)
            ret = 3;
        if (x > 432 && x <= 432 + 64)
            ret = 4;
        if (x > 496 && x <= 496 + 64)
            ret = 5;
        return ret;
        // nếu ret trả về -1. tức là mình click ở ngoài ô vuông thì sẽ hủy trạng thái
        // mình click vào các ô vuông trước đó
    }

    // không gian mà mình có thể click vào ô vuông theo chiều ngang
    // mỗi ô vuông có kích thước 64x64
    private int getMappingRow(int y) {
        int ret = -1;
        if (y > 236 && y <= 236 + 64)
            ret = 0;
        if (y > 300 && y <= 300 + 64)
            ret = 1;
        return ret;
        // nếu ret trả về -1. tức là mình click ở ngoài ô vuông thì sẽ hủy trạng thái
        // mình click vào các ô vuông trước đó
    }

    // giới hạn nè
    protected boolean isInBound(int c, int r) {
        // System.out.println(c + " " + r);
        return (c >= 0 && c <= 5) && (r == 0 || r == 1);
    }

    protected Direction handleChoosingDirection() {
        // TODO Auto-generated method stub
        return null;
    }

    // vẽ hướng đi
    protected void drawDirectionButton(int c, int r) {
        Graphics g = getGraphics(); // java swing component
        g.setColor(Color.red);
        int centerX = ((c * 64) / 2) + offSetX + (c - 1) * (32);
        int centerY = ((r * 64) / 2) + offSetY + (r) * 32;
        // System.out.println("draw at " + centerX + " " + centerY);
        centerY += 20;
        int[] setX1 = { centerX, centerX + 20, centerX + 20, centerX + 35, centerX + 20, centerX + 20, centerX };
        int[] setX2 = { centerX, centerX - 20, centerX - 20, centerX - 35, centerX - 20, centerX - 20, centerX };

        int[] setY1 = { centerY + 20, centerY + 20, centerY + 25, centerY + 15, centerY + 5, centerY + 10,
                centerY + 10 };
        int[] setY2 = { centerY + 20, centerY + 20, centerY + 25, centerY + 15, centerY + 5, centerY + 10,
                centerY + 10 };

        g.fillPolygon(setX1, setY1, 6);
        g.fillPolygon(setX2, setY2, 6);
        isShowingButtonDirection = !isShowingButtonDirection;
    }

    protected Point getRC(int x, int y) {
        return null;
    }

    private void initDefaultBackGround() {
        setPreferredSize(new Dimension(800, 600));
        defaultBackGround = new Image[3];
        defaultBackGround[0] = imgLoader.getImg("backGround.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(defaultBackGround[0], 0, 0, null);
        int centerX = (getPreferredSize().width / 2) - 3 * 64 + 32;
        int centerY = getPreferredSize().height / 2 - 64;
        g.setColor(Color.BLACK);

        g.drawArc(centerX - 64, centerY, 128, 128, 90, 180);
        // TODO draw boss1's square militaries
        drawMilitaries(centerX - 64, centerY, controller.getGameSquares()[0], true, g);

        for (int i = 0, newX = centerX, newY = centerY; i < 5; i++, newX += 64) {
            // System.out.println("1 Draw at " + newX + " " + newY);
            g.drawRect(newX, newY, 64, 64);
            // TODO draw militaries of 1
            drawmilitaries(newX, newY, controller.getGameSquares()[i + 1].getMilitaries(), g);

            g.drawRect(newX, newY + 64, 64, 64);
            // TODO draw militaries of 2
            drawmilitaries(newX, newY + 64,
                    controller.getGameSquares()[controller.getGameSquares().length - 1 - i].getMilitaries(), g);
            // System.out.println("2 Draw at " + newX + " " + (newY + 64));
        }

        g.drawArc(centerX + 4 * 64, centerY, 128, 128, 270, 180);
        // TODO draw boss2's square militaries
        drawMilitaries(centerX + 5 * 64 + 27, centerY, controller.getGameSquares()[6], false, g);

        // player's 1 inventory
        g.drawRect(250, 60, 128, 80);

        // TODO draw img that relate with militaries of pl2
        int player1militaries = controller.getMilitary(Player.PLAYER_2);
        drawmilitaries(250 + 32, 60, player1militaries, g);

        // player's 2 inventory
        g.drawRect(getPreferredSize().width - 250 - 128, getPreferredSize().height - 60 - 85, 128, 80);
        // TODO draw img that relate with militaries of pl1
        int player2militaries = controller.getMilitary(Player.PLAYER_1);
        drawmilitaries(getPreferredSize().width - 250 - 128 + 32, getPreferredSize().height - 60 - 85,
                player2militaries, g);
    }

    private void drawmilitaries(int x, int y, int playermilitaries, Graphics g) {
        String imgName = "";
        if (playermilitaries > 5)
            imgName = "5.png";
        else
            imgName = playermilitaries + ".png";
        Image loadedImg = imgLoader.getImg(imgName);
        g.drawImage(loadedImg, x, y, null);

        if (playermilitaries > 5) {
            g.drawString("+" + (playermilitaries - 5), x + 64 / 2, y + 64 / 2);
        }
    }

    private void drawMilitaries(int x, int y, GameSquare square, boolean isLeftBoss, Graphics g) {
        if (square.isBossSquare()) {
            if (isLeftBoss) {
                g.drawImage(imgLoader.getImg("boss.png"), x + 32, y + 5, null);
                drawmilitaries(x, y + 32, square.getMilitaries() - 10, g);
            } else {
                g.drawImage(imgLoader.getImg("boss.png"), x - 20, y + 90, null);
                drawmilitaries(x - 28, y + 32, square.getMilitaries() - 10, g);
            }

        } else {
            if (isLeftBoss)
                drawmilitaries(x, y + 15, square.getMilitaries(), g);
            else
                drawmilitaries(x - 28, y + 32, square.getMilitaries(), g);
        }
    }

    @Override
    public void toMessage(String mess) {
        Graphics g = getGraphics();
        g.setFont(new Font("tahoma", Font.ITALIC, 34));
        g.setColor(Color.red);
        g.drawString(mess, (getPreferredSize().width / 2) - ((mess.length() * 34) / 4),
                (getPreferredSize().height / 2));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateView(boolean isDelay) {
        try {
            paintImmediately(new Rectangle(getPreferredSize()));
            if (isDelay)
                Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
