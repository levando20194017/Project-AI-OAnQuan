package game.lib.AI;

import java.util.Iterator;

import game.model.Direction;
import game.model.GameBoard;
import game.model.GameSquare;
import game.model.Player;

public class Node {
   public Node parent;
   public GameBoard gameBoard;
   public Player p1, p2, currentPlayer;
   public int squareIndex;
   public Direction direction;
   public int h; // ~ heuristic
   private Node[] successors; // All valid moves from current state
   private boolean isMaximizing;
   public static int count = 0;

   public Node(Node parent, GameBoard gameBoard, Player p1, Player p2) {
      this.gameBoard = gameBoard;
      this.p1 = p1.currentPlayer();
      this.p2 = p2.currentPlayer();
      this.currentPlayer = this.p1;
      this.parent = parent;

   }

   public Node(GameBoard gameBoard, Player p1, Player p2) {
      this.gameBoard = gameBoard;
      this.p1 = p1.currentPlayer();
      this.currentPlayer = this.p1;
      this.p2 = p2.currentPlayer();
   }

   public Node(Node cur, Player curPlayer, Player other) {
      this.currentPlayer = curPlayer.currentPlayer();
      this.p1 = currentPlayer;
      this.p2 = other.currentPlayer();
      this.gameBoard = cur.gameBoard.cpy();
      this.h = cur.h;
      this.direction = cur.direction;
      this.squareIndex = cur.squareIndex;
      this.parent = cur.parent;
   }

   // Utility function
   public void evaluation() {
      Player computer = (p1.isComputer() ? p1 : p2);
      Player human = (p1.isComputer() ? p2 : p1);
      h = (computer.militaries) - (human.militaries);
      // System.out.println("in evaluation squareIndex: " + squareIndex
      // + " computer " + computer.militaries
      // + " human " + human.militaries);
   }

   public static void move(int squareIndex, Direction direction, Player curPlayer, GameBoard gameBoard) {
      final int BOSS_1_INDEX = 0;
      final int BOSS_2_INDEX = 6;

      // Can not move the militaries in boss square
      if (squareIndex == BOSS_1_INDEX || squareIndex == BOSS_2_INDEX)
         return;

      gameBoard.setLoopDirection(direction);
      gameBoard.setFirstIndexOfLoop(squareIndex);

      int militaries = gameBoard.getAndRemoveMilitaryAt(squareIndex); // số dân bốc lên để rải
      if (militaries == 0)
         return;

      Iterator<GameSquare> squares = gameBoard.iterator();
      while (militaries-- > 0) {
         GameSquare cur = squares.next();
         // cộng thêm 1 dân vào mỗi ô khi rải quân
         cur.setMilitaries(cur.getMilitaries() + 1);
      }

      GameSquare nextOfLastSquare = squares.next(); // next square of latestLoopSquare

      // check if next cell is empty
      if (nextOfLastSquare.getMilitaries() == 0
            && nextOfLastSquare.getSquareIndex() != BOSS_1_INDEX
            && nextOfLastSquare.getSquareIndex() != BOSS_2_INDEX) {

         GameSquare nextOfEmptySquare = squares.next(); // next square of empty square
         if (nextOfEmptySquare.isBossSquare()) {
            nextOfEmptySquare.setBossSquare(false);
         }

         int Militaries = gameBoard.getMilitaryAt(nextOfEmptySquare.getSquareIndex());
         if (Militaries > 0) {
            curPlayer.militaries += Militaries; // cộng điểm cho người chơi
            gameBoard.removeMiltaryAt(nextOfEmptySquare.getSquareIndex()); // xóa số dân vừa ăn được
            nextOfEmptySquare = squares.next(); // tiếp tục vòng lặp để xét trường hợp ăn sole cách nhau 1 ô trống
            while (nextOfEmptySquare.getMilitaries() == 0) {
               nextOfEmptySquare = squares.next();
               int n = gameBoard.getMilitaryAt(nextOfEmptySquare.getSquareIndex());
               if (n == 0) {
                  break;
               }
               if (nextOfEmptySquare.isBossSquare())
                  nextOfEmptySquare.setBossSquare(false);
               gameBoard.removeMiltaryAt(nextOfEmptySquare.getSquareIndex());
               curPlayer.militaries += n;
               nextOfEmptySquare = squares.next();
            }
         }
      }

      if (nextOfLastSquare.getMilitaries() != 0) {
         // nếu ô vuông kề với ô bị rải quân ở cuối cùng có dân thì tiếp tục rải
         move(nextOfLastSquare.getSquareIndex(), gameBoard.getLatestLoopDirection(), curPlayer, gameBoard);
      }
   }

   private Node moveSquare(Node parent, GameBoard gameBoard, int squareIndex, Direction direction) {
      if (gameBoard.getMilitaryAt(squareIndex) == 0) {
         return null;
      }
      Node res = new Node(parent, gameBoard, p1, p2);
      res.squareIndex = squareIndex;
      res.direction = direction;
      Node.move(res.squareIndex, res.direction, res.currentPlayer, res.gameBoard);
      res.evaluation();
      System.out.println("on moving " + res.p1 + " | " + res.p2
            + " squareIndex: " + res.squareIndex
            + " dir: " + res.direction.name()
            + " evaluation " + res.h);
      return res;
   }

   public Node[] successors() {
      if (successors == null)
         // a player has 5 squares, each square has 2 directions to choose from (left and
         // right)
         successors = new Node[5 * 2];
      else
         return successors;
      if (!gameBoard.stillHaveMilitaryOnBoard(currentPlayer)) {
         if (currentPlayer.militaries > 5) {
            // get 5 militaries of currentPlayer, put on board, one square each
            gameBoard.outMilitaries(currentPlayer);
         } else {
            gameBoard.outMilitariesIf(currentPlayer);
            // vay số dân từ người chơi đối phương để rải lên mỗi ô
         }
      }

      // run all of case
      if (currentPlayer.equals(Player.PLAYER_2)) {
         for (int i = 1, sIndex = 0; i < 6; i++) {
            // System.out.println("on pl2 move left and right");
            successors[sIndex++] = moveSquare(this, gameBoard.cpy(), i, Direction.LEFT);
            successors[sIndex++] = moveSquare(this, gameBoard.cpy(), i, Direction.RIGHT);
         }
         count++;
         System.out.println("count = " + count);
      }

      if (currentPlayer.equals(Player.PLAYER_1)) { // compare the name of player
         for (int i = 7, sIndex = 0; i < 12; i++) {
            // System.out.println("on pl1 move left and right");
            successors[sIndex++] = moveSquare(this, gameBoard.cpy(), i, Direction.LEFT);
            successors[sIndex++] = moveSquare(this, gameBoard.cpy(), i, Direction.RIGHT);
         }
         count++;
         System.out.println("count = " + count);
      }

      return successors;
   }

   @Override
   public String toString() {
      return gameBoard.toString();
   }

   public void setMaximizingTurn(boolean isMaximizing) {
      this.isMaximizing = isMaximizing;
   }

   public boolean isMaximizingTurn() {
      return this.isMaximizing;
   }

   public void swapPlayer() {
      currentPlayer = p2;
      p2 = p1;
      p1 = currentPlayer;

   }

   public static ComputerDecisionResult getPath(Node currentNode) {
      System.out.println("On getPath : currentNode == null ? " + (currentNode == null));
      Node rs = null;
      while (currentNode.parent != null) {
         rs = currentNode;
         currentNode = currentNode.parent;
      }
      return new ComputerDecisionResult(rs.squareIndex, rs.direction);

   }
}
