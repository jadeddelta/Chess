package tests.mailbox;

import boards.NaiveMailboxBoard;

/**
 * NaiveMailboxBoard test boards for testing various functions in debugging.
 */
public final class NaiveMailboxBoardExamples {
    // -------- DEBUG BOARDS ----------
    /** @return a board with a hanging queen about to get captured. */
    public static NaiveMailboxBoard getHangingQueenBoard() {
        NaiveMailboxBoard board = new NaiveMailboxBoard();
        board.setPieceAt(4, 4, -5);
        board.setPieceAt(3, 3, 1);
        board.setPieceAt(3, 1, 0);
        return board;
    }

    /** @return a board with a protected bishop that can be captured
     * by a queen. */
    public static NaiveMailboxBoard getProtectedBishopBoard() {
        int[] pbBoard = getKingPawnPairs();
        pbBoard[3 + 3*8] = 5;
        pbBoard[4 + 4*8] = -3;
        pbBoard[3 + 5*8] = -1;
        return new NaiveMailboxBoard(pbBoard);
    }

    // -------- TEST BOARDS ----------
    /** @return a board that tests if a pawn is blocked by an ally and an enemy,
     * followed by pawns that can do a double push but are blocked by an ally and an enemy. */
    public static NaiveMailboxBoard getPawnBlockBoard() {
        int[] pawnBlockTest = new int[64];
        pawnBlockTest[0] = 1; pawnBlockTest[8] = 4;
        pawnBlockTest[2] = 1; pawnBlockTest[10] = -4;
        pawnBlockTest[12] = 1; pawnBlockTest[28] = 4;
        pawnBlockTest[14] = 1; pawnBlockTest[30] = -4;
        return new NaiveMailboxBoard(pawnBlockTest);
    }

    public static NaiveMailboxBoard getPawnCaptureBoard() {
        int[] pawnCaptureTest = new int[64];

        return new NaiveMailboxBoard(pawnCaptureTest);
    }

    // ------- HELPER METHODS --------
    private static int[] getKingPawnPairs() {
        int[] kingPawnPairs = new int[64];
        kingPawnPairs[0] = 6; kingPawnPairs[8] = 1;
        kingPawnPairs[8*6] = -1; kingPawnPairs[8*7] = -6;
        return kingPawnPairs;
    }
}
