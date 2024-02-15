package engine.search;

import boards.Move;
import boards.NaiveMailboxBoard;
import engine.evaluation.Evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The search for SMELLY. Is only meant to search at a specific depth, and find the
 * best moves given the conditions set by the engine.
 */
public class MSearch {

    private int seconds;
    private int depth;
    private Evaluation evaluation;

    public MSearch(int seconds, int depth, Evaluation evaluation) {
        this.seconds = seconds;
        this.depth = depth;
        this.evaluation = evaluation;
    }

    public Move getBestMove(NaiveMailboxBoard board) {
        boolean isWhite = board.isWhiteTurn();
        Move bestMove = null;
        int bestMoveValue = isWhite ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Move m : board.getLegalMoves()) {
            NaiveMailboxBoard copy = new NaiveMailboxBoard(board);
            copy.executeMove(m);

            int moveValue = minimax(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, isWhite);

            if ((isWhite && moveValue > bestMoveValue) || (!isWhite && moveValue < bestMoveValue)) {
                bestMove = m.getClone();
                bestMoveValue = moveValue;
            }
        }
        return bestMove;
    }

    public HashMap<Move, Integer> getBestMoves(NaiveMailboxBoard board) {
        boolean isWhite = board.isWhiteTurn();
        HashMap<Move, Integer> map = new HashMap<>(100);
        for (Move m : board.getLegalMoves()) {
            NaiveMailboxBoard copy = new NaiveMailboxBoard(board);
            copy.executeMove(m);

            int moveValue = minimax(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, isWhite);

            map.put(m, moveValue);
        }
        return map;
    }

    public int minimax(NaiveMailboxBoard board, int alpha, int beta, int depth, boolean isWhite) {
        if (depth == 0)
            return evaluation.getPositionValue(board);
        
        int bestMoveValue = isWhite ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Move m : board.getLegalMoves()) {
            NaiveMailboxBoard copy = new NaiveMailboxBoard(board);
            copy.executeMove(m);

            int moveValue = minimax(copy, alpha, beta, depth - 1, !isWhite);

            if ((isWhite && moveValue > bestMoveValue) || (!isWhite && moveValue < bestMoveValue))
                bestMoveValue = moveValue;

            if (isWhite)
                alpha = Integer.max(alpha, bestMoveValue);
            else
                beta = Integer.min(beta, bestMoveValue);

            if (alpha >= beta)
                break;
        }
        return bestMoveValue;
    }

}
