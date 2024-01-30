package boards;

public class NaiveMailboxBoard extends Board {
    private final int BOARD_SIZE = 64;
    private boolean whiteQueenCastleFlag;
    private boolean whiteKingCastleFlag;
    private boolean blackQueenCastleFlag;
    private boolean blackKingCastleFlag;

    // + is white, - is black
    private int[] pieces;

    // default board
    public NaiveMailboxBoard() {
        this.pieces = new int[BOARD_SIZE];

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
    }

    public NaiveMailboxBoard(int[] pieces) {
        assert pieces.length == BOARD_SIZE;
        this.pieces = pieces;
    }

    public static void main(String[] args) {
        NaiveMailboxBoard b = new NaiveMailboxBoard();
        System.out.println(b);
    }

    public int[] getPieces() {
        return pieces;
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
        sb.append(System.lineSeparator());
        for (int i = 0; i < 8; i++) {
            sb.append(NUMBERS[i]);
            sb.append(" ");
        }
        return sb.toString();
    }
}
