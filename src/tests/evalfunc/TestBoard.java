package tests.evalfunc;

import java.util.ArrayList;

public class TestBoard {
    public static final int BOARD_SIZE = 64;

    // maybe chunk this down into a byte
    // for now, using a square-centric board association
    // 64-slot array with access (col, row) -> col + row * 8

    // 0 is empty, 1: pawn, 2: knight, 3: bishop, 4: rook, 5: queen, 6: king
    private int[] pieces;

    // 1 is white, 2 is black, 0 is empty
    private int[] colors;

    private ArrayList<TestMove> moves;

    public TestBoard(int[] pieces, int[] colors) {
        this.pieces = pieces;
        this.colors = colors;
        moves = new ArrayList<>();
    }

    public TestBoard() {
        pieces = new int[BOARD_SIZE];
        colors = new int[BOARD_SIZE];
        moves = new ArrayList<>();

        // color instantiate
        final int TWO_RANKS = BOARD_SIZE / 4;
        for (int i = 0; i < TWO_RANKS; i++) {
            colors[i] = 2;
            colors[i + TWO_RANKS] = 0;
            colors[i + 2*TWO_RANKS] = 0;
            colors[i + 3*TWO_RANKS] = 1;
        }

        // piece instantiate
        pieces[0] = pieces[7] = 4;
        pieces[1] = pieces[6] = 2;
        pieces[2] = pieces[5] = 3;
        pieces[3] = 5;
        pieces[4] = 6;

        for (int i = 8; i < 16; i++) {
            pieces[i] = 1;
        }
        for (int i = 16; i < 24; i++) {
            pieces[i] = 0;
        }
        for (int i = 24; i < 32; i++) {
            pieces[i] = 0;
        }
        for (int i = 48; i < BOARD_SIZE; i++) {
            pieces[i] = pieces[BOARD_SIZE - 1 - i];
        }
        pieces[59] = pieces[3];
        pieces[60] = pieces[4];
    }

    public void generateMoves(int x, int y) {
        TestMoveGenerator tmg = new TestMoveGenerator();
        moves = tmg.generateMoves(x, y, pieces, colors);
    }

    public int[] getPieces() {
        return pieces;
    }

    public int[] getColors() {
        return colors;
    }

    public ArrayList<TestMove> getMoves() {
        return moves;
    }

    private static String generate2DString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        final char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        final char[] NUMBERS = {'1', '2', '3', '4', '5', '6', '7', '8'};
        for (int y = 0; y < 8; y++) {
            sb.append(NUMBERS[7 - y]);
            sb.append("  ");
            for (int x = 0; x < 8; x++) {
                sb.append(arr[x + y*8]);
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("   ");
        for (int i = 0; i < 8; i++) {
            sb.append(LETTERS[7 - i]);
            sb.append(" ");
        }
        return sb.toString();
    }

    public String toString() {
        return generate2DString(pieces)
                + System.lineSeparator()
                + System.lineSeparator()
                + generate2DString(colors);
    }

    public static void main(String[] args) {
        TestBoard b = new TestBoard();
        System.out.println(b);
    }
}
