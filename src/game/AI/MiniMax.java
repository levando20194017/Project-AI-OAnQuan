package game.AI;

import game.model.GameBoard;
import game.model.Player;

public class MiniMax implements IAutoSearching {

    private ComputerDecisionResult minimaxDecision(GameBoard gameBoard, Player mover, Player p, int maxDepth) {
        if (maxDepth == 0)
            return null;
        Node currentNode = new Node(gameBoard, mover, p);
        int v = maxValue(currentNode, 0, maxDepth);

        for (Node node : currentNode.successors()) {
            if (node != null && v == node.h)
                return Node.getPath(node);
        }
        return null;
    }

    private int maxValue(Node currentNode, int currentNodeDepth, int maxDepth) {
        if (terminalTest(currentNode, currentNodeDepth, maxDepth)) {
            return currentNode.h;
        }

        int v = Integer.MIN_VALUE;
        for (Node successor : currentNode.successors()) {
            if (successor == null)
                continue;
            successor.swapPlayer();
            v = Math.max(v, minValue(successor, currentNodeDepth + 1, maxDepth));
        }
        currentNode.h = v;
        return v;
    }

    private int minValue(Node currentNode, int currentNodeDepth, int maxDepth) {
        if (terminalTest(currentNode, currentNodeDepth, maxDepth)) {
            return currentNode.h;
        }
        int v = Integer.MAX_VALUE;
        for (Node successor : currentNode.successors()) {
            if (successor == null)
                continue;
            successor.swapPlayer();
            v = Math.min(v, maxValue(successor, currentNodeDepth + 1, maxDepth));
        }
        currentNode.h = v;
        return v;
    }

    private boolean terminalTest(Node currentNode, int currentNodeDepth, int maxDepth) {
        if (currentNodeDepth == maxDepth)
            return true;
        return currentNode.gameBoard.isEndGame();
    }

    @Override
    public ComputerDecisionResult search(GameBoard gameBoard, Player Player1, Player player2, int maxDepth) {
        return minimaxDecision(gameBoard, Player1, player2, maxDepth);
    }
}
