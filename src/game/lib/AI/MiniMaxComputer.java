package game.lib.AI;

import game.model.GameBoard;
import game.model.Player;

public class MiniMaxComputer implements IAutoSearching {
    // mover variable is the next mover that is follow by initialize game board
    private ComputerDecisionResult minimaxDecision(GameBoard gameBoard, Player mover, Player op, int maxDepth) {
        if (maxDepth == 0)
            return null;
        Node currentNode = new Node(gameBoard, mover, op);
        int max = maxValue(currentNode, 0, maxDepth);

        for (Node n : currentNode.successors()) {
            if (n == null)
                continue;
            if (max == n.higher)
                return Node.getPath(n);
        }
        return null;
    }

    private int maxValue(Node currentNode, int currentNodeDepth, int maxDepth) {
        int v = Integer.MIN_VALUE;
        if (testTerminateNode(currentNode, currentNodeDepth, maxDepth)) {
            return currentNode.higher;
        }
        for (Node suc : currentNode.successors()) {
            if (suc == null)
                continue;
            suc.changePlayer();
            v = Math.max(v, findMinValue(suc, currentNodeDepth + 1, maxDepth));
        }
        currentNode.higher = v;
        return v;
    }

    private int findMinValue(Node currentNode, int currentNodeDepth, int maxDepth) {
        int v = Integer.MAX_VALUE;
        if (testTerminateNode(currentNode, currentNodeDepth, maxDepth)) {
            return currentNode.higher;
        }
        for (Node suc : currentNode.successors()) {
            if (suc == null)
                continue;
            suc.changePlayer();
            v = Math.min(v, maxValue(suc, currentNodeDepth + 1, maxDepth));
        }
        currentNode.higher = v;
        return v;
    }

    private boolean testTerminateNode(Node currentNode, int currentNodeDepth, int maxDepth) {
        if (currentNodeDepth == maxDepth)
            return true;
        return currentNode.gameBoard.isEndGame();
    }

    @Override
    public ComputerDecisionResult doSearch(GameBoard gameBoard, Player Player1, Player player2, int maxDepth) {
        return minimaxDecision(gameBoard, Player1, player2, maxDepth);
    }
}
