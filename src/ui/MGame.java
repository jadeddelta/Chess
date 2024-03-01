package ui;

import boards.Move;
import boards.NaiveMailboxBoard;
import engine.MEngine;
import engine.evaluation.MEvaluation;
import engine.search.MinABSearch;
import tests.mailbox.NaiveMailboxBoardExamples;

import java.util.Scanner;

/** The class that contains the heuristics for a chess game with one
 * human player and the SMELLY engine as a computer player. */
public class MGame {
    private MEngine engine;
    private NaiveMailboxBoard board = new NaiveMailboxBoard();
    private final static Scanner input = new Scanner(System.in);

    public MGame() {
        this(new MEngine());
    }

    public MGame(MEngine eng) {
        this.engine = eng;
    }

    public void startGame() {
        board = new NaiveMailboxBoard();
        System.out.println("welcome to the game, gamer");
        boolean isWhite;
        while (true) {
            System.out.println("white or black?");
            String s = input.nextLine().toLowerCase();
            if (s.equals("white") || s.equals("w")) {
                isWhite = true;
                break;
            }
            else if (s.equals("black") || s.equals("b")) {
                isWhite = false;
                break;
            } else {
                System.out.println("not an option, nerdo");
            }
        }
        System.out.println(board);
        if (!isWhite) {
            doComputerMove();
        }
        while (true) {
            if (doHumanMove() == 0) {
                System.out.println("you lost!");
                break;
            }
            if (doComputerMove() == 0) {
                System.out.println("you win!");
                break;
            }
        }
    }

    private int doHumanMove() {
        Move testMove;
        String start;
        String end;
        do {
            // TODO: enforce legal moves by the player
            System.out.println("What piece would you like to move?");
            start = input.nextLine().toLowerCase();
            System.out.println("Where would you like to move it to?");
            end = input.nextLine().toLowerCase();
        } while ((testMove = parseMoveFromAlgebraic(start, end)) == null);

        board.executeMove(testMove);
        printBoardUI(testMove);
        return board.getLegalMoves().size();
    }

    private int doComputerMove() {
        Move m = engine.doTurn(board);
        board.executeMove(m);
        printBoardUI(m);
        return board.getLegalMoves().size();
    }

    private Move parseMoveFromAlgebraic(String start, String end) {
        if (start.length() != end.length() || start.length() != 2)
            return null;
        char[] startFileRank = start.toCharArray();
        char[] endFileRank = end.toCharArray();
        int startX = convertChar(startFileRank[0]);
        int startY = convertChar(startFileRank[1]);
        int endX = convertChar(endFileRank[0]);
        int endY = convertChar(endFileRank[1]);
        if (startX == -1 || startY == -1 || endX == -1 || endY == -1)
            return null;
        else
            return new Move(
                    startX, startY,
                    endX, endY,
                    board.getPieceAt(startX, startY),
                    board.getPieceAt(endX, endY)
            );
    }

    /**
     * Returns the corresponding x or y value of the given character, accounting
     * for if it is a rank or file character.
     * @param c the char to be inspected
     * @return the numerical value of the char, -1 if invalid
     */
    private int convertChar(char c) {
        int num = Character.getNumericValue(c);
        if (num >= 1 && num <= 8) {
            return num - 1;
        } else if (num >= 10 && num <= 17) { // a=10, h=17
            return num - 10;
        } else {
            return -1;
        }
    }

    private void printBoardUI(Move m) {
        final String RESET = "\033[0m";
        final String BLACK = "\033[0;40m";
        final String WHITE = "\033[0;47m";

        double evaluation = ((double) engine.boardPosition(board)) / 100.0;

        String evalColor;
        if (evaluation == 0.0)
            evalColor = "";
        else
            evalColor = evaluation > 0.0 ? WHITE : BLACK;

        String evalString = "Position: " + evalColor + evaluation + RESET;
        // always called after board execute, so reverse
        String turnString = !board.isWhiteTurn() ? "White Plays" : "Black Plays";
        String moveString = m.toString();

        String[] outputLines = board.toString().split(System.lineSeparator());
        outputLines[2] = outputLines[2].replaceAll(System.lineSeparator(), "")
                + evalString;
        outputLines[4] = outputLines[4].replaceAll(System.lineSeparator(), "")
                + turnString;
        outputLines[5] = outputLines[5].replaceAll(System.lineSeparator(), "")
                + moveString;

        for (String s : outputLines)
            System.out.println(s);
        System.out.println();
    }

    // idea: hook into search directly to find out the best moves, maybe hook into a
    // debug function?
    public void _debugPosition(Move m) {

    }

    public static void main(String[] args) {
        NaiveMailboxBoard b = NaiveMailboxBoardExamples.getProtectedBishopBoard();
        MinABSearch s = new MinABSearch(-1, 4, new MEvaluation());
        System.out.println(b);
        System.out.println(s.getBestMove(b));
    }
}
