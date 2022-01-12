package game.lib.AI;

import game.model.GameBoard;
import game.model.Player;

public class MiniMaxComputer implements IAutoSearching {
    // mover variable is the next mover that is follow by initialize game board
    private ComputerDecisionResult doMiniMaxAl(GameBoard gb, Player mover, Player op, int maxDepth) {
        if (maxDepth == 0)
            return null;
        Node cur = new Node(gb, mover, op);
        int max = findMaxValue(cur, 0, maxDepth);

        for (Node n : cur.successors()) {
            if (n == null)
                continue;
            if (max == n.higher)
                return Node.getPath(n);
        }
        return null;
    }

    private int findMaxValue(Node cur, int curDepth, int maxDepth) {
        int v = Integer.MIN_VALUE;
        if (testTerminateNode(cur, curDepth, maxDepth)) {
            return cur.higher;
        }
        for (Node suc : cur.successors()) {
            if (suc == null)
                continue;
            suc.changePlayer();
            v = Math.max(v, findMinValue(suc, curDepth + 1, maxDepth));
        }
        cur.higher = v;
        return v;
    }

    private int findMinValue(Node cur, int curDepth, int maxDepth) {
        int v = Integer.MAX_VALUE;
        if (testTerminateNode(cur, curDepth, maxDepth)) {
            return cur.higher;
        }
        for (Node suc : cur.successors()) {
            if (suc == null)
                continue;
            suc.changePlayer();
            v = Math.min(v, findMaxValue(suc, curDepth + 1, maxDepth));
        }
        cur.higher = v;
        return v;
    }

    private boolean testTerminateNode(Node cur, int curDepth, int maxDepth) {
        if (curDepth == maxDepth)
            return true;
        return cur.gameBoard.isEndGame();
    }

    @Override
    public ComputerDecisionResult doSearch(GameBoard gb, Player Player1, Player player2, int maxDepth) {
        return doMiniMaxAl(gb, Player1, player2, maxDepth);
    }
}
