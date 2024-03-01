package engine;

import boards.Move;
import boards.NaiveMailboxBoard;
import engine.evaluation.Evaluation;
import engine.evaluation.MEvaluation;
import engine.search.MinABSearch;

/**
 * The engine for SMELLY. The engine is meant to handle time constraints and delegate
 * time to the engine, whilst also performing specific searches.
 */
public class MEngine {

    Evaluation evaluation;
    MinABSearch search;

    /** Sets up an engine with a search depth of 4, the default
     * evaluation function, and a MinABSearch algorithm. */
    public MEngine() {
        this(new MEvaluation());
    }

    /** Sets up an engine with a search depth of 4, a provided evaluation
     * function, and a MinABSearch algorithm. */
    public MEngine(Evaluation evaluation) {
        this.evaluation = evaluation;
        this.search = new MinABSearch(-1, 4, evaluation);
    }

    /** Sets up an engine with a custom search depth and a custom
     * evaluation function, with MinABSearch. */
    public MEngine(Evaluation evaluation, int depth) {
        this.evaluation = evaluation;
        this.search = new MinABSearch(-1, depth, evaluation);
    }

    /**
     * Handles getting the best move given the current board state and
     * current turn state (which is embedded in the board)
     * @param board the board state to do the turn on
     * @return the best possible move as determined by the search function
     */
    public Move doTurn(NaiveMailboxBoard board) {
        Move bestMove = search.getBestMove(board);
        // TODO: time constraints, etc...
        return bestMove;
    }

    /**
     * Returns the current evaluation function's guess on which
     * side is doing better.
     * @param board the board state to evaluate
     * @return the evaluation metric in centipawns
     */
    public int boardPosition(NaiveMailboxBoard board) {
        return evaluation.getPositionValue(board);
    }
}
