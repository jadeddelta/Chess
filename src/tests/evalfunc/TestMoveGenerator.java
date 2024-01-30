package tests.evalfunc;

import java.util.ArrayList;

// this is probably going to be very janky and unoptimized.
// this generator creates PARALEGAL moves: moves that can be done
// but we are ignoring if the king is in check
public class TestMoveGenerator {
    private int color;
    private int slot; // TODO: maybe consider a util function to compose/decompose pieces?
    private int pieces[];
    private int colors[];
    private ArrayList<TestMove> possibleMoves;

    private final int UP = 8;
    private final int DOWN = -8;
    private final int UPRIGHT = 9;
    private final int UPLEFT = 7;
    private final int DOWNRIGHT = -7;
    private final int DOWNLEFT = -9;
    private final int LEFT = -1;
    private final int RIGHT = 1;

    private final int[] ALL_DIRECTIONS = {UP, DOWN, UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT, LEFT, RIGHT};

    public TestMoveGenerator() {
        possibleMoves = new ArrayList<>();
    }

    // TODO: later is to integrate this with the board class
    // TODO: or wrap the board class up in such a way that we can access these things easily
    // TODO: (specifically the signature parameters x, y, pieces and colors)

    // idea: keep piece metrics in memory, generate EVERY move possible, then throw out
    // the ones that are blocked/collide
    public ArrayList<TestMove> generateMoves(int x, int y, int[] pieces, int[] colors) {
        possibleMoves.clear();
        int piece = pieces[x + y * 8];
        this.color = colors[x + y * 8];
        this.slot = x + y * 8;
        this.pieces = pieces;
        this.colors = colors;

        switch (piece) {
            case 1 -> getPawnMoves();
            case 2 -> getKnightMoves();
            case 3 -> getBishopMoves();
            case 4 -> getRookMoves();
            case 5 -> getQueenMoves();
            case 6 -> getKingMoves();
        }

        return possibleMoves;
    }

    // interesting note: en passant, we will have to encode each pawn
    // if it has double jumped or no

    // ** another interesting note: the conditions for en passant are
    // that if the double push pawn was made IMMEDIATELY before
    private void getPawnMoves() {
        boolean isWhite = (color == 1);
        int direction = isWhite ? UP : DOWN;
        int takeLeft = direction + LEFT + slot;
        int takeRight = direction + RIGHT + slot;
        boolean isStartPawn = isWhite ? (slot % 8 == 1) : (slot % 8 == 6);

        System.out.println(pieces[slot + direction]);
        System.out.println(colors[slot + direction]);

        if (colors[takeLeft] != color && colors[takeLeft] != 0)
            possibleMoves.add(new TestMove(slot, takeLeft));

        if (colors[takeRight] != color && colors[takeRight] != 0)
            possibleMoves.add(new TestMove(slot, takeRight));

        if (pieces[slot + direction] != 0) {// is there a piece in front of the pawn?
            return;
        }

        if (isStartPawn && colors[slot + 2*direction] == 0)
            possibleMoves.add(new TestMove(slot, slot + 2*direction));

        possibleMoves.add(new TestMove(slot, slot + direction));
    }

    private void getKnightMoves() {

    }

    private void getBishopMoves() {

    }

    private void getRookMoves() {

    }

    private void getQueenMoves() {

    }

    private void getKingMoves() {

    }

    private int[] decomposeSlot(int s) {
        return new int[]{s % 8, Math.floorDiv(s, 8)};
    }

    public static void main(String[] args) {
        int[] testpieces = new int[64];
        int[] colorpieces = new int[64];
        int slot = 3 + 5*8;

        testpieces[slot] = colorpieces[slot] = 1;
        TestBoard board = new TestBoard(testpieces, colorpieces);
        System.out.println(board);
        board.generateMoves(3, 5);
        System.out.println(board.getMoves());
    }
}
