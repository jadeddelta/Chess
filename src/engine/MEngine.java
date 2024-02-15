package engine;

import boards.Move;
import boards.NaiveMailboxBoard;
import engine.evaluation.Evaluation;
import engine.evaluation.MEvaluation;
import engine.search.MSearch;

/**
 * The engine for SMELLY. The engine is meant to handle time constraints and delegate
 * time to the engine, whilst also performing specific searches.
 */
public class MEngine {

    Evaluation evaluation;
    MSearch search;

    public MEngine(Evaluation evaluation) {
        this.evaluation = evaluation;
        this.search = new MSearch(-1, 4, evaluation);
    }

    public MEngine(Evaluation evaluation, int depth) {
        this.evaluation = evaluation;
        this.search = new MSearch(-1, depth, evaluation);
    }

    public Move doTurn(NaiveMailboxBoard board) {
        Move bestMove = search.getBestMove(board);
        // TODO: time constraints, etc...
        return bestMove;
    }

    public int boardPosition(NaiveMailboxBoard board) {
        return evaluation.getPositionValue(board);
    }
}
