package engine.evaluation;

import boards.Board;
import boards.NaiveMailboxBoard;

public abstract class Evaluation {

    public abstract int getPositionValue(NaiveMailboxBoard board);

    // TODO: when we abstract board properly, implement this!
    // public abstract int getPositionValue(Board board);
}
