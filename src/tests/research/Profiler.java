package tests.research;

import boards.Move;
import boards.NaiveMailboxBoard;
import engine.evaluation.MEvaluation;
import engine.search.MinABSearch;
import tests.mailbox.NaiveMailboxBoardExamples;
import tests.perft.NaiveMailboxPerft;

/**
 * The class that contains the main logic for testing the speed of various executions.
 */
public class Profiler {
    // ----- runnable tasks -----
    public abstract static class ProfilerRunnable implements Runnable {
        int numberOfAttempts;
        public ProfilerRunnable(int numberOfAttempts) {
            this.numberOfAttempts = numberOfAttempts;
        }

        @Override
        public void run() {
            long startTime = System.nanoTime();
            System.out.printf(
                    "Executing task %s %d times..." + System.lineSeparator(),
                    this.getClassName(),
                    numberOfAttempts
            );
            for (int i = 0; i < numberOfAttempts; i++) {
                doTask();
            }
            double timeToComplete = System.nanoTime() - startTime;
            double totalTime = timeToComplete / 1_000_000_000L;
            double avgTime = totalTime / numberOfAttempts;
            System.out.printf("Took %.8fs. Each call took an average of %.8fs." + System.lineSeparator(),
                    totalTime, avgTime);
        }

        public String getClassName() {
            String[] packageName = this.getClass().getName().split("\\.");
            return packageName[packageName.length - 1];
        }

        public abstract void doTask();
    }

    private static class CloneNMB extends ProfilerRunnable {
        public CloneNMB(int numberOfAttempts) { super(numberOfAttempts); }

        @Override
        public void doTask() {
            NaiveMailboxBoard nmb = new NaiveMailboxBoard();
            NaiveMailboxBoard clone = new NaiveMailboxBoard(nmb);
        }
    }

    private static class LegalMovesNMB extends Profiler.ProfilerRunnable {
        public LegalMovesNMB(int numberOfAttempts) { super(numberOfAttempts); }

        @Override
        public void doTask() {
            // vary between two boards for trying as many positions as possible.
            NaiveMailboxBoard nmb = (Math.random() > 0.5) ?
                    new NaiveMailboxBoard() :
                    NaiveMailboxBoardExamples.getPerftPosition2();
            nmb.getLegalMoves();
        }
    }

    private static class ExecuteMoveNMB extends Profiler.ProfilerRunnable {
        public ExecuteMoveNMB(int numberOfAttempts) { super(numberOfAttempts); }

        @Override
        public void doTask() {
            // make a dummy move, the position will never get evaluated after all
            int x = (int) (Math.random() * 8);
            int y = (int) (Math.random() * 8);
            int piece = (int) (Math.random() * 12) - 6;
            int capture = (int) (Math.random() * 12) - 6;
            // NOTE ^^^ java math.random uses an LCG and is very quick, should be negligible.
            NaiveMailboxBoard nmb = new NaiveMailboxBoard();
            nmb.executeMove(new Move(x, y, piece, capture));
        }
    }

    private static class EvaluateNMB extends Profiler.ProfilerRunnable {
        public EvaluateNMB(int numberOfAttempts) { super(numberOfAttempts); }

        @Override
        public void doTask() {
            NaiveMailboxBoard nmb = new NaiveMailboxBoard();
            MEvaluation eval = new MEvaluation();
        }
    }

    private static class SearchNMB extends Profiler.ProfilerRunnable {
        int depth;
        public SearchNMB(int numberOfAttempts, int depth) {
            super(numberOfAttempts);
            this.depth = depth;
        }

        @Override
        public void doTask() {
            NaiveMailboxBoard nmb = (Math.random() > 0.5) ?
                    new NaiveMailboxBoard() :
                    NaiveMailboxBoardExamples.getPerftPosition2();
            MEvaluation eval = new MEvaluation();
            MinABSearch search = new MinABSearch(-1, depth, eval);
            search.getBestMove(nmb);
        }

        @Override
        public String getClassName() {
            String[] packageName = this.getClass().getName().split("\\.");
            return packageName[packageName.length - 1] + " @ Depth=" + depth;
        }
    }

    /** dummy class used to get a template for profiling*/
    private static class NMB extends Profiler.ProfilerRunnable {
        public NMB(int numberOfAttempts) { super(numberOfAttempts); }

        @Override
        public void doTask() {
            NaiveMailboxBoard nmb = new NaiveMailboxBoard();
        }
    }

    /** class for profiling perft, custom constructor for different depths. */
    private static class PerftNMB extends Profiler.ProfilerRunnable {
        int depth;
        public PerftNMB(int numberOfAttempts, int depth) {
            super(numberOfAttempts);
            this.depth = depth;
        }

        @Override
        public void doTask() {
            NaiveMailboxBoard board = new NaiveMailboxBoard();
            NaiveMailboxPerft.perft(board, depth);
        }

        @Override
        public String getClassName() {
            String[] packageName = this.getClass().getName().split("\\.");
            return packageName[packageName.length - 1] + " @ Depth=" + depth;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ProfilerRunnable[] batch = {
                new CloneNMB(1000),
                new EvaluateNMB(1000),
                new LegalMovesNMB(1000),
                new ExecuteMoveNMB(1000),
                new SearchNMB(1000, 0),
                new SearchNMB(100, 2),
                new SearchNMB(100, 4)
        };

        for (ProfilerRunnable pr : batch) {
            Thread t = new Thread(pr);
            t.start();
            t.join();
        }

//        for (int i = 0; i < 7; i++) {
//            ProfilerRunnable perftRun = new PerftNMB(5, i);
//            Thread perftThread = new Thread(perftRun);
//            perftThread.start();
//            perftThread.join();
//        }
    }
}

//private static class NMB extends Profiler.ProfilerRunnable {
//    public NMB(int numberOfAttempts) { super(numberOfAttempts); }
//
//    @Override
//    public void doTask() {
//        NaiveMailboxBoard nmb = new NaiveMailboxBoard();
//    }
//}