package boards;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NaiveMailboxBoard extends Board {
    private final int BOARD_SIZE = 64;
    /** LEFT, UP, RIGHT, DOWN */
    private final int[] PLUS_DIRECTIONS = {-1, 8, 1, -8};
    /** UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT */
    private final int[] CROSS_DIRECTIONS = {7, 9, -7, -9};
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
    private int[] pieces;

    private Stack<Move> moveHistory;

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

        moveHistory = new Stack<>();
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

    public static void main(String[] args) {
        NaiveMailboxBoard b = new NaiveMailboxBoard();
        System.out.println(b);
        List<Move> legalMoveTest = b.getLegalMoves();
        System.out.println(legalMoveTest);
        System.out.println(legalMoveTest.size());
    }

    public int[] getPieces() {
        return pieces;
    }

    public void executeMove(Move m) {
        int pieceToCapture = pieces[m.getMailboxEnd()];
        pieces[m.getMailboxEnd()] = pieces[m.getMailboxStart()];
        pieces[m.getMailboxStart()] = 0;
        m.setCapturedPiece(pieceToCapture);
        moveHistory.push(m);
        isWhiteTurn = !isWhiteTurn;
    }

    public void undoMove() {
        Move m = moveHistory.pop();
        pieces[m.getMailboxStart()] = pieces[m.getMailboxEnd()];
        pieces[m.getMailboxEnd()] = m.getCapturedPiece();
        isWhiteTurn = !isWhiteTurn;
    }

    public List<Move> getLegalMoves() {
        List<Move> moves = this.getParalegalMoves();

        return moves;
    }

    private List<Move> getParalegalMoves() {
        List<Move> paralegalMoves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (isCurrentTurnPiece(i)) {
                List<Move> currentPieceMoves;
                switch (Math.abs(pieces[i])) {
                    case 1 -> //PAWN
                            currentPieceMoves = this.getPawnMoves(i);
                    case 2 -> //KNIGHT
                            currentPieceMoves = this.getKnightMoves(i);
                    case 3 -> //BISHOP
                            currentPieceMoves = this.getBishopMoves(i);
                    case 4 -> //ROOK
                            currentPieceMoves = this.getRookMoves(i);
                    case 5 -> //QUEEN
                            currentPieceMoves = this.getQueenMoves(i);
                    case 6 -> //KING
                            currentPieceMoves = this.getKingMoves(i);
                    default -> //SHOULD NEVER HAPPEN
                            currentPieceMoves = new ArrayList<>();
                }
                System.out.printf("Piece: %s with %d moves:%n", pieces[i], currentPieceMoves.size());
                System.out.println(currentPieceMoves);
                paralegalMoves.addAll(currentPieceMoves);
            }
        }
        return paralegalMoves;
    }

    private List<Move> getPawnMoves(int slot) {
        List<Move> pawnMoves = new ArrayList<>();
        int verticalDirection = isWhiteTurn ? PLUS_DIRECTIONS[1] : PLUS_DIRECTIONS[3];
        int captureLeft = isWhiteTurn ? CROSS_DIRECTIONS[1] : CROSS_DIRECTIONS[3];
        int captureRight = isWhiteTurn ? CROSS_DIRECTIONS[0] : CROSS_DIRECTIONS[2];
        int checkSlot;

        if (isEnemyPiece(checkSlot = slot + captureLeft))
            pawnMoves.add(new Move(slot, checkSlot));
        if (isEnemyPiece(checkSlot = slot + captureRight))
            pawnMoves.add(new Move(slot, checkSlot));

        if (isAnyPiece(checkSlot = slot + verticalDirection))
            return pawnMoves;
        else
            pawnMoves.add(new Move(slot, checkSlot));

        if (isAnyPiece(checkSlot = slot + 2*verticalDirection))
            return pawnMoves;
        else
            pawnMoves.add(new Move(slot, checkSlot));

        return pawnMoves;
    }

    private List<Move> getKnightMoves(int slot) {
        List<Move> knightMoves = new ArrayList<>();
        int x = slot % 8;
        int y = Math.floorDiv(slot, 8);

        for (int[] knight_2D_point : KNIGHT_2D_POINTS) {
            int checkX = x + knight_2D_point[0];
            int checkY = y + knight_2D_point[1];

            if ((checkX <= 7 && checkX >= 0) && (checkY <= 7 && checkY >= 0)) {
                int checkSlot = checkX + 8 * checkY;
                System.out.printf("x: %d, y: %d, ex: %d, ey: %d || s: %d, cs: %d, pcs: %d%n",
                        x, y, checkX, checkY, slot, checkSlot, pieces[checkSlot]);
                if (!isCurrentTurnPiece(checkSlot)) {
                    knightMoves.add(new Move(slot, checkSlot));
                }
            }
        }
//        int checkSlot;
//        for (int i : KNIGHT_DIRECTIONS) {
//            checkSlot = slot + i;
//            if (isValidSlot(checkSlot) && !isCurrentTurnPiece(checkSlot)) {
//                System.out.println(new Move(slot, checkSlot));
//                System.out.println(slot);
//                System.out.println(checkSlot);
//                knightMoves.add(new Move(slot, checkSlot));
//            }
//        }
        return knightMoves;
    }

    private List<Move> getBishopMoves(int slot) {
        List<Move> bishopMoves = new ArrayList<>();
        int checkSlot;
        for (int i : CROSS_DIRECTIONS) {
            checkSlot = slot + i;
            for (; isValidSlot(checkSlot); checkSlot += i) {
                if (isCurrentTurnPiece(checkSlot)) // blocked by friendly
                    break;
                if (isEnemyPiece(checkSlot)) { // blocked by enemy
                    bishopMoves.add(new Move(slot, checkSlot));
                    break;
                }
                bishopMoves.add(new Move(slot, checkSlot));
            }
        }
        return bishopMoves;
    }

    private List<Move> getRookMoves(int slot) {
        List<Move> rookMoves = new ArrayList<>();
        int checkSlot;
        for (int i : PLUS_DIRECTIONS) {
            checkSlot = slot + i;
            for (; isValidSlot(checkSlot); checkSlot += i) {
                if (isCurrentTurnPiece(checkSlot)) // blocked by friendly
                    break;
                if (isEnemyPiece(checkSlot)) { // blocked by enemy
                    rookMoves.add(new Move(slot, checkSlot));
                    break;
                }
                rookMoves.add(new Move(slot, checkSlot));
            }
        }
        return rookMoves;
    }

    private List<Move> getQueenMoves(int slot) {
        List<Move> queenMoves = new ArrayList<>();
        queenMoves.addAll(getRookMoves(slot));
        queenMoves.addAll(getBishopMoves(slot));
        return queenMoves;
    }

    private List<Move> getKingMoves(int slot) {
        List<Move> kingMoves = new ArrayList<>();
        int checkSlot;
        for (int i : CROSS_DIRECTIONS) {
            checkSlot = slot + i;
            if (!isValidSlot(checkSlot) || isCurrentTurnPiece(checkSlot))
                continue;
            kingMoves.add(new Move(slot, checkSlot));
        }
        for (int i : PLUS_DIRECTIONS) {
            checkSlot = slot + i;
            if (!isValidSlot(checkSlot) || isCurrentTurnPiece(checkSlot))
                continue;
            kingMoves.add(new Move(slot, checkSlot));
        }
        return kingMoves;
    }

    private boolean isAnyPiece(int slot) {
        return pieces[slot] != 0;
    }

    private boolean isCurrentTurnPiece(int slot) {
        if (pieces[slot] == 0) return false;
        else return pieces[slot] > 0 == isWhiteTurn;
    }

    private boolean isEnemyPiece(int slot) {
        if (pieces[slot] == 0) return false;
        else return pieces[slot] > 0 != isWhiteTurn;
    }

    private boolean isValidSlot(int slot) {
        return (slot >= 0) && (slot < 63);
    }

    @Override
    public String toString() {
        // 0 is bottom left, +1 right, +8 up
        final char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        final char[] NUMBERS = {'1', '2', '3', '4', '5', '6', '7', '8'};
        StringBuilder sb = new StringBuilder();
        for (int y = 7; y >= 0; y--) {
            sb.append(LETTERS[y]);
            sb.append(" ");
            for (int x = 7; x >= 0; x--) {
                int piece = pieces[x + y * 8];
                if (piece == 0)
                    sb.append("   ");
                else if (piece > 0) {
                    sb.append("  ");
                    sb.append(piece);
                }
                else {
                    sb.append(" ");
                    sb.append(piece);
                }
            }
            sb.append(System.lineSeparator());
        }
        sb.append("    ");
        for (int i = 0; i < 8; i++) {
            sb.append(NUMBERS[i]);
            sb.append("  ");
        }
        return sb.toString();
    }
}
