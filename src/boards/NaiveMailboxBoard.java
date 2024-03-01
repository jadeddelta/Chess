package boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * The initial implementation for the Mailbox board representation,
 * with move generation included.
 */
public class NaiveMailboxBoard extends Board {
    private final int BOARD_SIZE = 64;
    /** RIGHT, UP, LEFT, DOWN */
    private final int[][] PLUS_2D_POINTS = {
            {1, 0}, {0, 1}, {-1, 0}, {0, -1}
    };
    /** UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT */
    private final int[][] CROSS_2D_POINTS = {
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };
    /** from top right, clockwise, of pair {x, y}*/
    private final int[][] KNIGHT_2D_POINTS = {
            {1, 2}, {2, 1}, {2, -1}, {1, -2},
            {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
    };
    private boolean whiteQueenCastleFlag;
    private boolean whiteKingCastleFlag;
    private boolean blackQueenCastleFlag;
    private boolean blackKingCastleFlag;
    private boolean isWhiteTurn;
    private boolean enPassantFlag;

    // + is white, - is black
    private final int[] pieces;

    private final Stack<Move> moveHistory;

    final char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    final char[] NUMBERS = {'1', '2', '3', '4', '5', '6', '7', '8'};

    // default board
    public NaiveMailboxBoard() {
        this.pieces = new int[BOARD_SIZE];
        whiteQueenCastleFlag = true;
        whiteKingCastleFlag = true;
        blackQueenCastleFlag = true;
        blackKingCastleFlag = true;
        isWhiteTurn = true;

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
            pieces[i] = -pieces[BOARD_SIZE - 1 - i];
        }

        pieces[59] = -pieces[3];
        pieces[60] = -pieces[4];

        moveHistory = new Stack<>();
    }

    // TODO: maybe refactor into a Setup object with all these fields?
    public NaiveMailboxBoard(int[] pieces) {
        this(pieces, new Stack<>());
    }

    public NaiveMailboxBoard(int[] pieces, Stack<Move> moveHistory) {
        this(pieces, true, true, true, true, true, false, moveHistory);
    }

    public NaiveMailboxBoard(int[] pieces, boolean isWhiteTurn) {
        this(pieces, true, true, true, true, isWhiteTurn, false, new Stack<>());
    }

    public NaiveMailboxBoard(int[] pieces,
                             boolean whiteQueenCastleFlag,
                             boolean whiteKingCastleFlag,
                             boolean blackQueenCastleFlag,
                             boolean blackKingCastleFlag,
                             boolean isWhiteTurn,
                             boolean enPassantFlag,
                             Stack<Move> moveHistory) {
        assert pieces.length == BOARD_SIZE;
        this.pieces = pieces;
        this.whiteQueenCastleFlag = whiteQueenCastleFlag;
        this.whiteKingCastleFlag = whiteKingCastleFlag;
        this.blackQueenCastleFlag = blackQueenCastleFlag;
        this.blackKingCastleFlag = blackKingCastleFlag;
        this.isWhiteTurn = isWhiteTurn;
        this.enPassantFlag = enPassantFlag;
        this.moveHistory = moveHistory;
    }

    /** clone constructor */
    public NaiveMailboxBoard(NaiveMailboxBoard board) {
        int[] newPieces = new int[BOARD_SIZE];
        System.arraycopy(board.getPieces(), 0, newPieces, 0, BOARD_SIZE);
        this.pieces = newPieces;
        this.whiteQueenCastleFlag = board.getWhiteQueenCastleFlag();
        this.whiteKingCastleFlag = board.getWhiteKingCastleFlag();
        this.blackQueenCastleFlag = board.getBlackQueenCastleFlag();
        this.blackKingCastleFlag = board.getBlackKingCastleFlag();
        this.isWhiteTurn = board.isWhiteTurn();
        this.enPassantFlag = board.getEnPassantFlag();
        Stack<Move> newHistory = new Stack<>();
        for (Move m : board.getMoveHistory()) {
            newHistory.push(m.getClone());
        }
        //newHistory.sort(Collections.reverseOrder());
        //TODO: right now, does not matter the history. but we should still
        // find a good way to copy this.
        this.moveHistory = newHistory;
    }

    public static void main(String[] args) {
        int[] test = new int[64];
        test[11] = 1;
        test[12] = 1;
        test[13] = 1;
        test[25] = 1;
        test[25 + 9] = -1;
        test[48] = -1;
        NaiveMailboxBoard b = new NaiveMailboxBoard(test);
        System.out.println(b);
        System.out.println(b.getLegalMoves());
    }

    public void executeMove(Move m) {
        pieces[m.getMailboxEnd()] = m.getInitialPiece();
        pieces[m.getMailboxStart()] = 0;
        moveHistory.push(m);
        isWhiteTurn = !isWhiteTurn;
    }

    public void undoMove() {
        Move m = moveHistory.pop();
        pieces[m.getMailboxStart()] = m.getInitialPiece();
        pieces[m.getMailboxEnd()] = m.getCapturedPiece();
        isWhiteTurn = !isWhiteTurn;
    }

    public List<Move> getLegalMoves() {
        List<Move> moves = this.getAllMoves();
        // equivalent to filtering move -> isCheckedMove(move)
        moves.removeIf(this::isCheckedMove);
        return moves;
    }

    private List<Move> getAllMoves() {
        List<Move> allMoves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (hasAllyPiece(i)) {
                allMoves.addAll(
                        getPseudolegalMoves(i)
                );
            }
        }
        return allMoves;
    }

    private boolean isEnemyKingInCheck() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (hasAllyPiece(i)) {
                for (Move m : getPseudolegalMoves(i)) {
                    if (m.isCheckMove()) {
//                        System.out.printf(
//                                "check found when doing %s: %s%n",
//                                moveHistory.peek().toNotation(),
//                                m.toNotation());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckedMove(Move m) {
        boolean isChecked;
        this.executeMove(m); // so now is enemy turn
        isChecked = isEnemyKingInCheck();
        this.undoMove();
        return isChecked;
    }

    private List<Move> getPseudolegalMoves(int slot) {
        switch (Math.abs(pieces[slot])) {
            case 1 -> {
                return this.getPawnMoves(slot);
            }
            case 2 -> {
                return this.getKnightMoves(slot);
            }
            case 3 -> {
                return this.getBishopMoves(slot);
            }
            case 4 -> {
                return this.getRookMoves(slot);
            }
            case 5 -> {
                return this.getQueenMoves(slot);
            }
            case 6 -> {
                return this.getKingMoves(slot);
            }
        }
        System.err.println("can't get here");
        return new ArrayList<>();
    }

    private List<Move> getPawnMoves(int slot) {
        List<Move> pawnMoves = new ArrayList<>(4);
        boolean isFirstMove = isWhiteTurn ?
                Math.floorDiv(slot, 8) == 1 :
                Math.floorDiv(slot, 8) == 6;
        int[] verticalDir = isWhiteTurn ? PLUS_2D_POINTS[1] : PLUS_2D_POINTS[3];
        int[] captureLeftDir = isWhiteTurn ? CROSS_2D_POINTS[1] : CROSS_2D_POINTS[3];
        int[] captureRightDir = isWhiteTurn ? CROSS_2D_POINTS[0] : CROSS_2D_POINTS[2];

        int checkSlot;

        // check left capture, right capture
        if ((checkSlot = isValidMove(slot, captureLeftDir, 1)) != -99
                && hasEnemyPiece(checkSlot))
            pawnMoves.add(new Move(slot, checkSlot, pieces[slot], pieces[checkSlot]));
        if ((checkSlot = isValidMove(slot, captureRightDir, 1)) != -99
                && hasEnemyPiece(checkSlot))
            pawnMoves.add(new Move(slot, checkSlot, pieces[slot], pieces[checkSlot]));

        // is slot directly in front invalid or ok?
        if ((checkSlot = isValidMove(slot, verticalDir, 1)) == -99
                || hasAnyPiece(checkSlot))
            return pawnMoves;
        else
            pawnMoves.add(new Move(slot, checkSlot, pieces[slot]));

        // check for first pawn's double push
        if ((checkSlot = isValidMove(slot, verticalDir, 2)) == -99
                || hasAnyPiece(checkSlot)
                || !isFirstMove)
            return pawnMoves;
        else
            pawnMoves.add(new Move(slot, checkSlot, pieces[slot]));

        return pawnMoves;
    }

    private List<Move> getKnightMoves(int slot) {
        List<Move> knightMoves = new ArrayList<>(8);
        int checkSlot;

        for (int[] knightDir : KNIGHT_2D_POINTS) {
            if ((checkSlot = isValidMove(slot, knightDir, 1)) != -99)
                if (!hasAllyPiece(checkSlot))
                    knightMoves.add(new Move(slot, checkSlot, pieces[slot], pieces[checkSlot]));
        }
        return knightMoves;
    }

    private List<Move> getBishopMoves(int slot) {
        List<Move> bishopMoves = new ArrayList<>(14);
        int checkSlot;

        for (int[] crossDir : CROSS_2D_POINTS) {
            int i = 1;
            while ((checkSlot = isValidMove(slot, crossDir, i)) != -99) {
                if (hasAllyPiece(checkSlot))
                    break;
                if (hasEnemyPiece(checkSlot)) {
                    bishopMoves.add(new Move(slot, checkSlot, pieces[slot], pieces[checkSlot]));
                    break;
                }
                bishopMoves.add(new Move(slot, checkSlot, pieces[slot]));
                i++;
            }
        }
        return bishopMoves;
    }

    private List<Move> getRookMoves(int slot) {
        List<Move> rookMoves = new ArrayList<>(14);
        int checkSlot;

        for (int[] plusDir : PLUS_2D_POINTS) {
            int i = 1;
            while ((checkSlot = isValidMove(slot, plusDir, i)) != -99) {
                if (hasAllyPiece(checkSlot))
                    break;
                if (hasEnemyPiece(checkSlot)) {
                    rookMoves.add(new Move(slot, checkSlot, pieces[slot], pieces[checkSlot]));
                    break;
                }
                rookMoves.add(new Move(slot, checkSlot, pieces[slot]));
                i++;
            }
        }
        return rookMoves;
    }

    private List<Move> getQueenMoves(int slot) {
        List<Move> queenMoves = new ArrayList<>(28);
        queenMoves.addAll(getRookMoves(slot));
        queenMoves.addAll(getBishopMoves(slot));
        return queenMoves;
    }

    private List<Move> getKingMoves(int slot) {
        List<Move> kingMoves = new ArrayList<>(8);
        int checkSlot;
        for (int[] crossDir : CROSS_2D_POINTS) {
            if ((checkSlot = isValidMove(slot, crossDir, 1)) != -99)
                if (!hasAllyPiece(checkSlot))
                    kingMoves.add(new Move(slot, checkSlot, pieces[slot], pieces[checkSlot]));
        }
        for (int[] plusDir : PLUS_2D_POINTS) {
            if ((checkSlot = isValidMove(slot, plusDir, 1)) != -99)
                if (!hasAllyPiece(checkSlot))
                    kingMoves.add(new Move(slot, checkSlot, pieces[slot], pieces[checkSlot]));
        }
        return kingMoves;
    }

    private List<Move> getPossibleSlidingMoves(int slot, int[][] dirs) {
        List<Move> validMoves = new ArrayList<>(28); // max moves is queen w/ 28
        int checkSlot;

        for (int[] dir : dirs) {
            int i = 1;
            // possible optimization: what's the maximum amount of steps, and
            // if i > max, then end.

            // adds max comparisons, removes 2 integer div, 2 mod, 1 add, 1 mult, 4 comparisons
            while ((checkSlot = isValidMove(slot, dir, i)) != -99) {
                if (hasAllyPiece(checkSlot))
                    break;
                if (hasEnemyPiece(checkSlot)) {
                    validMoves.add(new Move(slot, checkSlot, pieces[slot], pieces[checkSlot]));
                    break;
                }
                validMoves.add(new Move(slot, checkSlot, pieces[slot]));
                i++;
            }
        }
        return validMoves;
    }


    /**
     * Checks if a move in a certain direction is valid, and returns the mailbox slot
     * of the endpoint.
     * @param slot the initial slot
     * @param direction the direction of the move in an {x, y} int[] pair
     * @param factor how much the direction should be multiplied by
     * @return the endpoint of the move, or -99 if the move is invalid
     */
    private int isValidMove(int slot, int[] direction, int factor) {
        int x = slot % 8;
        int y = Math.floorDiv(slot, 8);
        int checkX = x + factor * direction[0];
        int checkY = y + factor * direction[1];
        if (isValidPoint(checkX, checkY)) {
            return pointToSlot(checkX, checkY);
        }
        else
            return -99;
    }

    private boolean hasAnyPiece(int slot) {
        return pieces[slot] != 0;
    }

    private boolean hasAllyPiece(int slot) {
        if (pieces[slot] == 0) return false;
        else return pieces[slot] > 0 == isWhiteTurn;
    }

    private boolean hasEnemyPiece(int slot) {
        if (pieces[slot] == 0) return false;
        else return pieces[slot] > 0 != isWhiteTurn;
    }

    private boolean isValidSlot(int slot) {
        return (slot >= 0) && (slot <= 63);
    }

    private boolean isValidPoint(int x, int y) {
        return (x >= 0) && (x <= 7) && (y >= 0) && (y <= 7);
    }

    private int pointToSlot(int x, int y) {
        return x + 8*y;
    }

    // TODO OPTIMIZE: create a field that tracks number of pieces
    public int getNumberOfPieces() {
        int ret = 0;
        for (int i : pieces) {
            if (i != 0) ret++;
        }
        return ret;
    }

    // TODO OPTIMIZE: these two functions can be replaced when we can make piecelists
    public List<Integer> getWhitePieces() {
        List<Integer> list = new ArrayList<>(16);
        for (int i : pieces) {
            if (i > 0) list.add(i);
        }
        return list;
    }

    public List<Integer> getBlackPieces() {
        List<Integer> list = new ArrayList<>(16);
        for (int i : pieces) {
            if (i < 0) list.add(i);
        }
        return list;
    }

    @Override
    public String toString() {
        return dumpBoard(true); // edit to debug when necessary
    }

    /** @return the board string, with color or without */
    public String dumpBoard(boolean withColor) {
        // 0 is bottom left, +1 right, +8 up
        StringBuilder sb = new StringBuilder();
        for (int y = 7; y >= 0; y--) {
            sb.append(NUMBERS[y]);
            sb.append(" ");
            for (int x = 0; x <= 7; x++) {
                int piece = pieces[x + y * 8];
                if (piece == 0)
                    sb.append("    ");
                else {
                    sb.append(withColor ? " " : "  ");
                    if (!withColor && piece > 0)
                        sb.append(" ");
                    sb.append(withColor ? getColorPiece(piece) : piece);
                }
            }
            sb.append(System.lineSeparator());
        }
        sb.append("    ");
        if (!withColor)
            sb.append(" ");
        for (int i = 0; i < 8; i++) {
            sb.append(LETTERS[i]);
            sb.append("   ");
        }
        return sb.toString();
    }

    /** @return a string representation of the piece with ANSI coloring */
    public String getColorPiece(int piece) {
        final String RESET = "\033[0m";
        final String BLACK = "\033[0;40m";
        final String WHITE = "\033[0;47m";

        return (piece > 0 ? WHITE + " " : BLACK) +
                piece + " " + RESET;
    }

    /**
     * Turns a <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation">FEN</a>
     * position into a NaiveMailboxBoard
     *
     * @param fen the FEN string to be processed
     * @return the board corresponding to the FEN string
     * @throws IllegalArgumentException if the characters in the board field of the FEN string are invalid
     */
    public static NaiveMailboxBoard getBoardFromFEN(String fen) throws IllegalArgumentException {
        String[] fields = fen.split(" ");
        assert fields.length == 6;
        String[] fenBoard = fields[0].split("/");
        assert fenBoard.length == 8;

        int[] board = new int[64];
        int i = 0;
        for (int r = 7; r >= 0; r--) {
            char[] rank = fenBoard[r].toCharArray();
            for (char piece : rank) {
                if (Character.isDigit(piece)) {
                    int emptySpaces = Character.getNumericValue(piece);
                    i += emptySpaces;
                } else {
                    board[i] = getPieceFromLetter(piece);
                    i++;
                }
            }
        }

        boolean isWhiteTurn = fields[1].equals("w");
        //TODO: use remaining fields
        // castling: KQkq -> bool fields
        // en passant: idk, wait until we implement en passant (b/c the field is a square)
        // halfmove clock: wait until we implement halfmove clock
        // fullmove number: maybe fill the movehistory with dummy moves?
        return new NaiveMailboxBoard(board, isWhiteTurn);
    }

    /** turns a character into the integer representation of the piece */
    private static int getPieceFromLetter(char c) throws IllegalArgumentException {
        int piece;
        boolean isCapitalized = Character.isUpperCase(c);
        switch (Character.toLowerCase(c)) {
            case 'k' -> piece = 6;
            case 'q' -> piece = 5;
            case 'r' -> piece = 4;
            case 'b' -> piece = 3;
            case 'n' -> piece = 2;
            case 'p' -> piece = 1;
            default -> throw new IllegalArgumentException();
        }
        return isCapitalized ? piece : -piece;
    }

    /** @return the piece at the given x, y coordinate */
    public int getPieceAt(int x, int y) {
        return pieces[pointToSlot(x, y)];
    }

    public void setPieceAt(int x, int y, int piece) {
        pieces[pointToSlot(x, y)] = piece;
    }

    public int[] getPieces() {
        return pieces;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public boolean getBlackKingCastleFlag() {
        return blackKingCastleFlag;
    }

    public boolean getBlackQueenCastleFlag() {
        return blackQueenCastleFlag;
    }

    public boolean getWhiteKingCastleFlag() {
        return whiteKingCastleFlag;
    }

    public boolean getWhiteQueenCastleFlag() {
        return whiteQueenCastleFlag;
    }

    public boolean getEnPassantFlag() {
        return enPassantFlag;
    }

    public Stack<Move> getMoveHistory() {
        return moveHistory;
    }
}
