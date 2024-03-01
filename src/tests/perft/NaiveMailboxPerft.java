package tests.perft;

import boards.Move;
import boards.NaiveMailboxBoard;
import tests.mailbox.NaiveMailboxBoardExamples;

import java.util.List;

/**
 * Class holding the basic implementation of the perft function
 * for a mailbox implementation
 *
 * @see <a href="https://www.chessprogramming.org/Perft">Link</a>
 * to see pseudocode that this was adapted from
 */
public class NaiveMailboxPerft {

    final long[] PERFT_VALS = {
            1L, 20L, 400L, 8902L, 197281L, 4865609L, 119060324L
    };

    public static long perft(NaiveMailboxBoard board, int depth) {
        if (depth == 0) {
            return 1L;
        }
        long nodes = 0L;
        List<Move> moveList = board.getLegalMoves();

        for (Move m : moveList) {
            board.executeMove(m);
            nodes += perft(board, depth - 1);
            board.undoMove();
        }

        return nodes;
    }

    public static void testSuite(NaiveMailboxBoard board, int depth) {
        for (int i = 1; i <= depth; i++) {
            System.out.println(perft(board, i));
        }
    }

    // TODO: 3+ on std. board inaccurate
    public static void main(String[] args) {
        NaiveMailboxBoard board = new NaiveMailboxBoard();
        NaiveMailboxBoard kiwi = NaiveMailboxBoardExamples.getPerftPosition2();
        System.out.println("STANDARD BOARD PERFT");
        testSuite(board, 5);
        System.out.println("KIWIPETE BOARD PERFT");
        testSuite(kiwi, 4);
    }

    /*
    CURRENT STATUS AS OF MOST RECENT COMMIT
    (depth, predicted, actual), prepend an X if it is wrong
    STANDARD BOARD:
    (1, 20, 20), (2, 400, 400), X(3, 8895, 8902), X(4, 196772, 197821)
    X(5, 4839076, 4865609), X(6, 117876267, 119060324), 7+ takes too long

    KIWIPETE BOARD:
    X(1, 46, 48), X(2, 1823, 2039), X(3, 84423, 97862), X(4, 3326476, 4085603)
     */
}
