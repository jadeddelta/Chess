package boards;

import java.io.IOException;

/**
 * A class designed to organize a move in chess.
 * NOTE: This was initially developed for the mailbox implementation.
 * A separate move class might need to be made for the BitBoard.
 */
public class Move {
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    /** (for mailbox) color specific piece at the {startX, startY} position */
    private final int initialPiece;
    /** (for mailbox) color specific piece at the {endX, endY} position */
    private final int capturedPiece;
    /** change between chess notation (true) or x,y pairings (false) */
    private final static boolean _DEBUG_STRING_NOTATION = true;
    /** for printing moves */
    final String[] RANKS = {"a", "b", "c", "d", "e", "f", "g", "h"};

    /** constructor for an {x,y} -> {x,y} move that does not capture */
    public Move(int startX, int startY, int endX, int endY, int initialPiece) {
        this(startX, startY, endX, endY, initialPiece, 0);
    }

    /** constructor for an {x,y} -> {x,y} move that does capture a piece */
    public Move(int startX, int startY, int endX, int endY, int initialPiece, int capturedPiece) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.initialPiece = initialPiece;
        this.capturedPiece = capturedPiece;
    }

    /** constructor for a mailbox move that doesn't capture */
    public Move(int startSlot, int endSlot, int initialPiece) {
        this(startSlot, endSlot, initialPiece, 0);
    }

    /** constructor for a mailbox move that does capture a piece */
    public Move(int startSlot, int endSlot, int initialPiece, int capturedPiece) {
        this.startX = startSlot % 8;
        this.startY = Math.floorDiv(startSlot, 8);
        this.endX = endSlot % 8;
        this.endY = Math.floorDiv(endSlot, 8);
        this.initialPiece = initialPiece;
        this.capturedPiece = capturedPiece;
    }

    public int getMailboxStart() {
        return startX + startY * 8;
    }

    public int getMailboxEnd() {
        return endX + endY * 8;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getCapturedPiece() {
        return capturedPiece;
    }

    public int getInitialPiece() {
        return initialPiece;
    }

    /** @return if the move would capture the king */
    public boolean isCheckMove() {
        return (Math.abs(capturedPiece) == 6);
    }

    public Move getClone() {
        return new Move(this.startX, this.startY, this.endX, this.endY, this.initialPiece, this.capturedPiece);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof Move m) {
            return (this.getMailboxStart() == m.getMailboxStart()) &&
                    (this.getMailboxEnd() == m.getMailboxEnd()) &&
                    (this.getCapturedPiece() == m.getCapturedPiece());
        }
        return false;
    }

    @Override
    public String toString() {
        return _DEBUG_STRING_NOTATION ?
                toNotation() :
                toCoordPairs();
    }

    /** @return a string using [x, y] pairs (starting from 0) between the start and finish position */
    public String toCoordPairs() {
        return "([" + startX + ", " + startY + "] -> [" +
                endX + ", " + endY + "] | " + initialPiece + " -> " + capturedPiece + ")";
    }

    /** @return a string using chess spatial notation between the start and finish position */
    public String toNotation() {
        return String.format("(%s%d -> %s%d | %d -> %d)",
                RANKS[startX],
                startY + 1,
                RANKS[endX],
                endY + 1,
                initialPiece,
                capturedPiece);
    }

    // TODO: toAlgebraicNotation
}
