package game.lib.AI;

import java.util.ArrayList;
import java.util.List;

import game.model.GameBoard;
import game.model.Player;

public class AlphaBetaPruningComputer implements IAutoSearching {
    // i define alpha is maximizing player that is computer, another is player

    @Override
    public ComputerDecisionResult doSearch(GameBoard gb, Player player1, Player player2, int maxDepth) {
        return doAlphaBetaPrunning(gb, player1, player2, maxDepth);
    }

    private ComputerDecisionResult doAlphaBetaPrunning(GameBoard gb, Player player1, Player player2, int maxDepth) {
        int alpha = Integer.MIN_VALUE; // maximizing
        int beta = Integer.MAX_VALUE; //

        Node root = new Node(gb, player1, player2);

        alpha = findMaxValue(root, alpha, beta, 0, maxDepth);

        List<Node> ap = new ArrayList<>();
        for (Node child : root.successors()) {
            if (child == null)
                continue;
            if (child.higher == alpha) {
                ap.add(child);
                return Node.getPath(child);
            }
        }
        return null;
    }

    private int findMinValue(Node cur, int alpha, int beta, int curDepth, int maxDepth) {
        if (beta <= alpha)
            return Integer.MAX_VALUE;
        if (testTerminatedNode(cur, curDepth, maxDepth))
            return cur.higher;

        for (Node child : cur.successors()) {
            if (child == null)
                continue;
            child.changePlayer();
            beta = Math.min(beta, findMaxValue(child, alpha, beta, curDepth + 1, maxDepth));
        }
        cur.higher = beta;
        return beta;
    }

    private int findMaxValue(Node cur, int alpha, int beta, int curDepth, int maxDepth) {
        if (beta <= alpha)
            return Integer.MIN_VALUE;
        if (testTerminatedNode(cur, curDepth, maxDepth))
            return cur.higher;

        for (Node child : cur.successors()) {
            if (child == null)
                continue;
            child.changePlayer();
            alpha = Math.max(alpha, findMinValue(child, alpha, beta, curDepth + 1, maxDepth));
        }
        cur.higher = alpha;
        return alpha;
    }

    private boolean testTerminatedNode(Node child, int curDepth, int maxDepth) {
        if (curDepth == maxDepth)
            return true;
        return child.gameBoard.isEndGame();
    }

}
