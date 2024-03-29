package tests.mailbox;

import boards.Move;
import boards.NaiveMailboxBoard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// TODO: do we even need this?
class NaiveMailboxBoardTest {


    static NaiveMailboxBoard pawnCaptureBoard;
    static NaiveMailboxBoard knightBlockBoard;
    static NaiveMailboxBoard knightCaptureBoard;
    static NaiveMailboxBoard bishopBlockBoard;
    static NaiveMailboxBoard bishopCaptureBoard;
    static NaiveMailboxBoard rookBlockBoard;
    static NaiveMailboxBoard rookCaptureBoard;

    @BeforeAll
    public static void setup() {
        int[] pawnCaptureTest = new int[64];
        int[] knightBlockTest = new int[64];
        int[] knightCaptureTest = new int[64];
        int[] bishopBlockText = new int[64];
        int[] bishopCaptureText = new int[64];
        int[] rookBlockTest = new int[64];
        int[] rookCaptureTest = new int[64];
        rookCaptureTest[0] = 4;
        rookCaptureTest[9] = -1;
        rookCaptureTest[54] = -2;
        rookCaptureTest[6] = -2;
        rookCaptureTest[8] = -1;
        rookCaptureBoard = new NaiveMailboxBoard(rookCaptureTest);
    }

    @Test
    public void detectPawnMoves() {
        NaiveMailboxBoard pawnBlockBoard = NaiveMailboxBoardExamples.getPawnBlockBoard();
        List<Move> pbbMoves = pawnBlockBoard.getLegalMoves();

        // first pawn is blocked by an ally
        Move firstPawn_testBlock = new Move(0, 8, 1);
        assertFalse(pbbMoves.contains(firstPawn_testBlock));
        Move firstPawn_testBlockDouble = new Move(0, 16, 1);
        assertFalse(pbbMoves.contains(firstPawn_testBlockDouble));

        // second pawn is blocked by an enemy
        Move secondPawn_testBlock = new Move(2, 10, 1);
        assertFalse(pbbMoves.contains(secondPawn_testBlock));
        Move secondPawn_testBlockDouble = new Move(2, 18, 1);
        assertFalse(pbbMoves.contains(secondPawn_testBlockDouble));

        // third pawn can move at least one square, but not double push b/c of ally
        Move thirdPawn_testMoveOnce = new Move(12, 20, 1);
        assertTrue(pbbMoves.contains(thirdPawn_testMoveOnce));
        Move thirdPawn_testBlockDouble = new Move(12, 28, 1);
        assertFalse(pbbMoves.contains(thirdPawn_testBlockDouble));

        // fourth pawn can move at least once square, but not double push b/c of enemy
        Move fourthPawn_testMoveOnce = new Move(14, 22, 1);
        assertTrue(pbbMoves.contains(fourthPawn_testMoveOnce));
        Move fourthPawn_testBlockDouble = new Move(14, 30, 1);
        assertFalse(pbbMoves.contains(fourthPawn_testBlockDouble));

        NaiveMailboxBoard pawnCaptureBoard = NaiveMailboxBoardExamples.getPawnCaptureBoard();
        List<Move> pcbMoves = pawnCaptureBoard.getLegalMoves();

        // white pawns can capture
        Move whitePawn_captureLeft = new Move(1, 1, 0, 2, 1, -4);
        assertTrue(pcbMoves.contains(whitePawn_captureLeft)); //TODO: doesn't work? maybe we found a bug
        Move whitePawn_captureRight = new Move(1, 1, 2, 2, 1, -4);
        assertTrue(pcbMoves.contains(whitePawn_captureRight));

        // black pawns can capture
        Move blackPawn_captureLeft = new Move(6, 6, 5, 5, -1, 4);
        assertTrue(pcbMoves.contains(blackPawn_captureLeft));
        Move blackPawn_captureRight = new Move(6, 6, 7, 5, -1, 4);
        assertTrue(pcbMoves.contains(blackPawn_captureRight));
    }
}