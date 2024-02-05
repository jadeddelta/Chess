package tests.perft;

import boards.Move;
import boards.NaiveMailboxBoard;

import java.util.List;

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

    public static void main(String[] args) {
        NaiveMailboxBoard board = new NaiveMailboxBoard();
        System.out.println(perft(board, 0));
        System.out.println(perft(board, 1));
        System.out.println(perft(board, 2));
        System.out.println(perft(board, 3)); //TODO: doesn't work!
    }
}
