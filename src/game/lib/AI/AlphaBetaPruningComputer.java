package game.lib.AI;

import java.util.ArrayList;
import java.util.List;

import game.model.GameBoard;
import game.model.Player;

public class AlphaBetaPruningComputer implements IAutoSearching {

    private ComputerDecisionResult alphaBetaSearch(GameBoard gameBoard, Player player1, Player player2, int maxDepth) {
        int alpha = Integer.MIN_VALUE; // maximizing
        int beta = Integer.MAX_VALUE;

        Node root = new Node(gameBoard, player1, player2);

        alpha = maxValue(root, alpha, beta, 0, maxDepth);

        List<Node> ap = new ArrayList<>();
        for (Node child : root.successors()) {
            if (child != null && child.h == alpha) {
                ap.add(child);
                return Node.getPath(child);
            }
        }
        return null;
    }

    private int minValue(Node cur, int alpha, int beta, int curDepth, int maxDepth) {
        if (beta <= alpha)
            return Integer.MAX_VALUE;
        if (testTerminatedNode(cur, curDepth, maxDepth))
            return cur.h;

        for (Node child : cur.successors()) {
            if (child == null)
                continue;
            child.swapPlayer();
            beta = Math.min(beta, maxValue(child, alpha, beta, curDepth + 1, maxDepth));
        }
        cur.h = beta;
        return beta;
    }

    private int maxValue(Node cur, int alpha, int beta, int curDepth, int maxDepth) {
        if (beta <= alpha)
            return Integer.MIN_VALUE;
        if (testTerminatedNode(cur, curDepth, maxDepth))
            return cur.h;

        for (Node child : cur.successors()) {
            if (child == null)
                continue;
            child.swapPlayer();
            alpha = Math.max(alpha, minValue(child, alpha, beta, curDepth + 1, maxDepth));
        }
        cur.h = alpha;
        return alpha;
    }

    private boolean testTerminatedNode(Node child, int curDepth, int maxDepth) {
        if (curDepth == maxDepth)
            return true;
        return child.gameBoard.isEndGame();
    }

    @Override
    public ComputerDecisionResult search(GameBoard gameBoard, Player player1, Player player2, int maxDepth) {
        return alphaBetaSearch(gameBoard, player1, player2, maxDepth);
    }

}
