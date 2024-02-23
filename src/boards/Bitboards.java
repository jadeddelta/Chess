package boards;
import java.util.HashMap;
import java.util.Map;

// TODO: find the minimum features needed to represent a board in order to abstract it
public class Bitboards {
    //variables used to assign bit spots
    private static final long A1 = 1L;
    private static final long B1 = 1L << 1;
    private static final long C1 = 1L << 2;
    private static final long D1 = 1L << 3;
    private static final long E1 = 1L << 4;
    private static final long F1 = 1L << 5;
    private static final long G1 = 1L << 6;
    private static final long H1 = 1L << 7;

    private static final long A2 = 1L << 8;
    private static final long B2 = 1L << 9;
    private static final long C2 = 1L << 10;
    private static final long D2 = 1L << 11;
    private static final long E2 = 1L << 12;
    private static final long F2 = 1L << 13;
    private static final long G2 = 1L << 14;
    private static final long H2 = 1L << 15;

    private static final long A3 = 1L << 16;
    private static final long B3 = 1L << 17;
    private static final long C3 = 1L << 18;
    private static final long D3 = 1L << 19;
    private static final long E3 = 1L << 20;
    private static final long F3 = 1L << 21;
    private static final long G3 = 1L << 22;
    private static final long H3 = 1L << 23;

    private static final long A4 = 1L << 24;
    private static final long B4 = 1L << 25;
    private static final long C4 = 1L << 26;
    private static final long D4 = 1L << 27;
    private static final long E4 = 1L << 28;
    private static final long F4 = 1L << 29;
    private static final long G4 = 1L << 30;
    private static final long H4 = 1L << 31;

    private static final long A5 = 1L << 32;
    private static final long B5 = 1L << 33;
    private static final long C5 = 1L << 34;
    private static final long D5 = 1L << 35;
    private static final long E5 = 1L << 36;
    private static final long F5 = 1L << 37;
    private static final long G5 = 1L << 38;
    private static final long H5 = 1L << 39;

    private static final long A6 = 1L << 40;
    private static final long B6 = 1L << 41;
    private static final long C6 = 1L << 42;
    private static final long D6 = 1L << 43;
    private static final long E6 = 1L << 44;
    private static final long F6 = 1L << 45;
    private static final long G6 = 1L << 46;
    private static final long H6 = 1L << 47;

    private static final long A7 = 1L << 48;
    private static final long B7 = 1L << 49;
    private static final long C7 = 1L << 50;
    private static final long D7 = 1L << 51;
    private static final long E7 = 1L << 52;
    private static final long F7 = 1L << 53;
    private static final long G7 = 1L << 54;
    private static final long H7 = 1L << 55;

    private static final long A8 = 1L << 56;
    private static final long B8 = 1L << 57;
    private static final long C8 = 1L << 58;
    private static final long D8 = 1L << 59;
    private static final long E8 = 1L << 60;
    private static final long F8 = 1L << 61;
    private static final long G8 = 1L << 62;
    private static final long H8 = 1L << 63;


    // Define bitboards for each piece type
    public long whitePawns;
    long whiteKnights;
    long whiteBishops;
    long whiteRooks;
    long whiteQueen;
    long whiteKing;

    long blackPawns;
    long blackKnights;
    long blackBishops;
    long blackRooks;
    long blackQueen;
    long blackKing;


    /**
     * initializing bitboards
     */
    public Bitboards (){
        initializeStartingPosition();
    }

    //macros, bit logic
//    public long popBit(long bitBoard, long square) {
//        // Ensure the square is within the board boundaries
//        // Ensure the square is set in the bitBoard
//        if ((bitBoard & square) == 0) {
//            throw new IllegalArgumentException("Square not set in the bitboard");
//        }
//        // Use bitwise AND with the complement of the square to clear it
//        return bitBoard & ~square;
//    }

    /**
     * initializing the starting position of the game and the bitboards
     */
    private void initializeStartingPosition(){

        this.whitePawns = A2 | B2 | C2 | D2 | E2 | F2 | G2 | H2;
        this.blackPawns = A7 | B7 | C7 | D7 | E7 | F7 | G7 | H7;

        this.whiteBishops = C1 | F1;
        this.blackBishops = C8 | F8;

        this.whiteKnights = B1 | G1;
        this.blackKnights = B8 | G8;

        this.whiteRooks = A1 | H1;
        this.blackRooks = A8 | H8;

        this.whiteQueen = D1;
        this.blackQueen = D8;

        this.whiteKing = E1;
        this.blackKing = E8;
    }


    /**
     * toString method that will print out the board. Capital letters are white pieces and lowercase letters are black pieces
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" +---+---+---+---+---+---+---+---+\n");
        for (int rank = 7; rank >= 0; rank--) {
            sb.append(rank + 1).append("| ");
            for (int file = 0; file < 8; file++) {
                long mask = 1L << (rank * 8 + file);
                char piece = ' ';
                if ((this.whitePawns & mask) != 0) piece = 'P';
                else if ((this.whiteKnights & mask) != 0) piece = 'N';
                else if ((this.whiteBishops & mask) != 0) piece = 'B';
                else if ((this.whiteRooks & mask) != 0) piece = 'R';
                else if ((this.whiteQueen & mask) != 0) piece = 'Q';
                else if ((this.whiteKing & mask) != 0) piece = 'K';
                else if ((this.blackPawns & mask) != 0) piece = 'p';
                else if ((this.blackKnights & mask) != 0) piece = 'n';
                else if ((this.blackBishops & mask) != 0) piece = 'b';
                else if ((this.blackRooks & mask) != 0) piece = 'r';
                else if ((this.blackQueen & mask) != 0) piece = 'q';
                else if ((this.blackKing & mask) != 0) piece = 'k';
                sb.append(piece).append(" | ");
            }
            sb.append("\n +---+---+---+---+---+---+---+---+\n");
        }
        sb.append("   a   b   c   d   e   f   g   h\n");
        return sb.toString();
    }

}
