package boards;

public class Move {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    // mailbox constructor
    public Move(int startSlot, int endSlot) {
        this.startX = startSlot % 8;
        this.startY = Math.floorDiv(startSlot, 8);
        this.endX = endSlot % 8;
        this.endY = Math.floorDiv(endSlot, 8);
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

    @Override
    public String toString() {
        return "[" + startX + ", " + startY + "] -> [" +
                endX + ", " + endY + "]";
    }
}
